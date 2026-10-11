import { Alert, Card, Col, Row } from 'react-bootstrap'
import { useAuth } from '../auth/AuthContext'

export default function Dashboard() {
  const { user } = useAuth()
  return <><h1 className="page-title">Dashboard</h1>
    <Alert variant="success">Welcome, {user?.fullName}. You are authenticated.</Alert>
    <Row className="g-3">
      <Col md={4}><Card className="h-100"><Card.Body><Card.Title>Your roles</Card.Title>{user?.roles?.join(', ') || 'No roles'}</Card.Body></Card></Col>
      <Col md={8}><Card className="h-100"><Card.Body><Card.Title>Your permissions</Card.Title>{user?.permissions?.length ? user.permissions.join(', ') : 'No additional permissions'}</Card.Body></Card></Col>
    </Row>
    <p className="text-secondary mt-4">Access to API endpoints is checked by Spring Security, not only by the frontend.</p>
  </>
}
