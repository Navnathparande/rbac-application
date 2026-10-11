# RBAC Application — Spring Boot + React + MySQL

Starter project with separate `backend/` and `frontend/` folders.

## Prerequisites
- Java 17+
- Maven 3.9+
- Node.js 20+
- MySQL 8+

## 1. Database
Run in MySQL Workbench:
```sql
CREATE DATABASE rbac_db;
CREATE USER 'rbac_user'@'localhost' IDENTIFIED BY 'ChangeThisPassword!';
GRANT ALL PRIVILEGES ON rbac_db.* TO 'rbac_user'@'localhost';
FLUSH PRIVILEGES;
```
Change credentials in `backend/src/main/resources/application.properties` to match your local setup.

## 2. Backend
```bash
cd backend
mvn spring-boot:run
```
API base URL: `http://localhost:8080/api`

On first startup, the app seeds roles and permissions and creates an initial admin if configured. Set `RBAC_ADMIN_EMAIL` and `RBAC_ADMIN_PASSWORD` environment variables before running. Change the initial password immediately.

## 3. Frontend
In a second terminal:
```bash
cd frontend
npm install
npm run dev
```
Open the Vite URL shown in the terminal.

## Security notes
- Passwords are BCrypt-hashed.
- JWT is sent as a Bearer token and stored in sessionStorage for this learning starter. For production, consider a secure HttpOnly cookie design and CSRF protections appropriate to that design.
- Never expose the JWT signing secret or database password in source control.
- The sample initial admin is created from environment variables; do not use a default production password.
- This is a learning starter, not a security-audited production system.
