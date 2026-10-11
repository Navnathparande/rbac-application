import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { Alert, Button, Card, Form } from 'react-bootstrap'
import { useAuth } from '../auth/AuthContext'

export default function Login() {
  const { login } = useAuth(), navigate = useNavigate()
  const [email, setEmail] = useState(''), [password, setPassword] = useState('')
  const [error, setError] = useState('')
  async function submit(e) {
    e.preventDefault(); setError('')
    try { await login(email, password); navigate('/') }
    catch { setError('Login failed. Check your email and password.') }
  }
  return <div className="auth-wrap"><Card className="auth-card shadow-sm"><Card.Body>
    <h2 className="mb-1">RBAC Portal</h2><p className="text-secondary">Sign in to continue</p>
    {error && <Alert variant="danger">{error}</Alert>}
    <Form onSubmit={submit}>
      <Form.Group className="mb-3"><Form.Label>Email</Form.Label><Form.Control type="email" required value={email} onChange={e=>setEmail(e.target.value)} /></Form.Group>
      <Form.Group className="mb-3"><Form.Label>Password</Form.Label><Form.Control type="password" required value={password} onChange={e=>setPassword(e.target.value)} /></Form.Group>
      <Button type="submit" className="w-100">Login</Button>
    </Form>
    <p className="mt-3 mb-0">New user? <Link to="/register">Create account</Link></p>
  </Card.Body></Card></div>
}
