import { Navigate, NavLink, Route, Routes, useNavigate } from 'react-router-dom'
import { Button, Container, Nav, Navbar } from 'react-bootstrap'
import { AuthProvider, useAuth } from './auth/AuthContext'
import ProtectedRoute from './components/ProtectedRoute'
import Login from './pages/Login'
import Register from './pages/Register'
import Dashboard from './pages/Dashboard'
import Users from './pages/Users'
import Forbidden from './pages/Forbidden'

function Layout() {
  const { user, logout } = useAuth(), navigate = useNavigate()
  return <>
    {user && <Navbar bg="dark" data-bs-theme="dark" expand="lg"><Container>
      <Navbar.Brand as={NavLink} to="/">RBAC Portal</Navbar.Brand>
      <Nav className="me-auto">
        <Nav.Link as={NavLink} to="/">Dashboard</Nav.Link>
        {(user.roles?.includes('ADMIN') || user.permissions?.includes('USER_READ')) &&
          <Nav.Link as={NavLink} to="/users">Users</Nav.Link>}
      </Nav>
      <span className="text-light me-3">{user.fullName}</span>
      <Button variant="outline-light" size="sm" onClick={() => { logout(); navigate('/login') }}>Logout</Button>
    </Container></Navbar>}
    <Container className="py-4">
      <Routes>
        <Route path="/login" element={user ? <Navigate to="/" /> : <Login />} />
        <Route path="/register" element={user ? <Navigate to="/" /> : <Register />} />
        <Route path="/" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
        <Route path="/users" element={<ProtectedRoute permission="USER_READ"><Users /></ProtectedRoute>} />
        <Route path="/forbidden" element={<Forbidden />} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </Container>
  </>
}
export default function App() { return <AuthProvider><Layout /></AuthProvider> }
