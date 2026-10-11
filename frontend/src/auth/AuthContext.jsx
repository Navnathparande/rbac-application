import { createContext, useContext, useMemo, useState } from 'react'
import api from '../api/client'

const AuthContext = createContext(null)
export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    try { return JSON.parse(sessionStorage.getItem('rbac_user')) } catch { return null }
  })
  async function login(email, password) {
    const { data } = await api.post('/auth/login', { email, password })
    sessionStorage.setItem('rbac_token', data.token)
    sessionStorage.setItem('rbac_user', JSON.stringify(data))
    setUser(data)
  }
  async function register(payload) {
    const { data } = await api.post('/auth/register', payload)
    sessionStorage.setItem('rbac_token', data.token)
    sessionStorage.setItem('rbac_user', JSON.stringify(data))
    setUser(data)
  }
  function logout() {
    sessionStorage.removeItem('rbac_token')
    sessionStorage.removeItem('rbac_user')
    setUser(null)
  }
  const value = useMemo(() => ({ user, login, register, logout }), [user])
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}
export const useAuth = () => useContext(AuthContext)
