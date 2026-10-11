import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { Alert, Button, Card, Form } from 'react-bootstrap'
import { useAuth } from '../auth/AuthContext'

export default function Register() {
  const { register } = useAuth(), navigate = useNavigate()
  const [fullName, setName] = useState(''), [email, setEmail] = useState(''), [password, setPassword] = useState('')
  const [error, setError] = useState('')
  async function submit(e) {
    e.preventDefault(); setError('')
    try { await register({ fullName, email, password }); navigate('/') }
    catch (err) { setError(err.response?.data?.message || 'Registration failed') }
  }
  return <div className="auth-wrap"><Card className="auth-card shadow-sm"><Card.Body>
    <h2>Create account</h2>{error && <Alert variant="danger">{error}</Alert>}
    <Form onSubmit={submit}>
      <Form.Group className="mb-3"><Form.Label>Full name</Form.Label><Form.Control required maxLength={100} value={fullName} onChange={e=>setName(e.target.value)} /></Form.Group>
      <Form.Group className="mb-3"><Form.Label>Email</Form.Label><Form.Control type="email" required value={email} onChange={e=>setEmail(e.target.value)} /></Form.Group>
      <Form.Group className="mb-3"><Form.Label>Password (minimum 8 characters)</Form.Label><Form.Control type="password" minLength={8} required value={password} onChange={e=>setPassword(e.target.value)} /></Form.Group>
      <Button type="submit" className="w-100">Register</Button>
    </Form><p className="mt-3 mb-0">Already registered? <Link to="/login">Login</Link></p>
  </Card.Body></Card></div>
}
