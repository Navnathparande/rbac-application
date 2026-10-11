import { useEffect, useState } from 'react'
import { Alert, Badge, Spinner, Table } from 'react-bootstrap'
import api from '../api/client'
import { useAuth } from '../auth/AuthContext'

export default function Users() {
  const { user } = useAuth()
  const [rows, setRows] = useState([]), [error, setError] = useState(''), [loading, setLoading] = useState(true)
  useEffect(() => {
    let active = true
    api.get('/users').then(r => { if(active) setRows(r.data) })
      .catch(e => { if(active) setError(e.response?.status === 403 ? 'You do not have permission to view users.' : 'Could not load users.') })
      .finally(() => { if(active) setLoading(false) })
    return () => { active = false }
  }, [])
  if (loading) return <Spinner animation="border" />
  return <><h1 className="page-title">User Management</h1>
    {error && <Alert variant="danger">{error}</Alert>}
    {!error && <Table responsive striped bordered hover><thead><tr><th>ID</th><th>Name</th><th>Email</th><th>Roles</th><th>Status</th></tr></thead>
      <tbody>{rows.map(row => <tr key={row.id}><td>{row.id}</td><td>{row.fullName}</td><td>{row.email}</td><td>{row.roles.join(', ')}</td><td><Badge bg={row.enabled?'success':'secondary'}>{row.enabled?'Active':'Disabled'}</Badge></td></tr>)}</tbody>
    </Table>}
    <small className="text-secondary">Signed in as {user?.email}</small>
  </>
}
