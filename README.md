# ⚙️ Spring Boot Authentication Service — Username + Password + JWT

This project is a **Spring Boot 3 (Java 21)** authentication microservice that provides secure user registration, login, and stateless authentication using **JWT (JSON Web Tokens)** and **MongoDB**.

---

## 🚀 Overview

This service implements a modern authentication flow:

- Users **register** with a username and password
- They **log in** using their credentials
- The backend issues a **JWT token** (instead of maintaining sessions)
- Clients use that token to **authenticate future requests**

JWT (JSON Web Token) is the **industry standard** for stateless authentication between frontend ↔ backend systems.

---

## 🔄 Authentication Flow

### 📝 1. Register
- The user sends `username` and `password`
- The password is **hashed (BCrypt)** and stored securely in **MongoDB**

### 🔐 2. Login
- The user sends their `username` and `password`
- If credentials are valid:
    - The backend generates a **JWT token** (signed with your secret key)
    - Returns it to the client (frontend, mobile app, etc.)

### 📡 3. Authenticated Requests
- The client includes the token in every HTTP request header:
    
    `Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`

- The `JwtAuthenticationFilter` intercepts each request:
  - Validates the token
  - Extracts the username
  - Injects the user’s identity into Spring Security’s context

This design keeps the service **stateless** — no session data stored in memory or database.

---

## 🧰 Tech Stack

- **Java 21**
- **Spring Boot 3**
- **Spring Security**
- **MongoDB (Atlas or Local)**
- **JWT (JSON Web Tokens)**
- **Lombok**

---

## 🧩 Key Features

- ✅ User Registration with hashed passwords
- ✅ Login endpoint returning signed JWT
- ✅ Token-based authentication (no sessions)
- ✅ Password change endpoint
- ✅ Secure, stateless API design

---

## 🧪 Example Requests

### ➕ Register

```bash
POST /api/auth/register
Content-Type: application/json

Request Body:
{
  "username": "john",
  "password": "mypassword"
}

```
### 🔑 Login
```bash
POST /api/auth/login
Content-Type: application/json

Request Body:
{
  "username": "john",
  "password": "mypassword"
}


Response:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

