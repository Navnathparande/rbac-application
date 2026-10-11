import { Link } from 'react-router-dom'
export default function Forbidden() { return <div className="text-center py-5"><h1>403 — Access denied</h1><p>Your account does not have permission to view this page.</p><Link to="/">Return to dashboard</Link></div> }
