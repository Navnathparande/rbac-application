import { Navigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'

export default function ProtectedRoute({ children, permission, role }) {
  const { user } = useAuth()
  if (!user) return <Navigate to="/login" replace />
  if (role && !user.roles?.includes(role) && !user.roles?.includes('ADMIN'))
    return <Navigate to="/forbidden" replace />
  if (permission && !user.permissions?.includes(permission) && !user.roles?.includes('ADMIN'))
    return <Navigate to="/forbidden" replace />
  return children
}
