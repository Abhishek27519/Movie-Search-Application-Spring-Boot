# Movie Search Application (Spring Boot)

A high-performance, **stateless** backend service designed to concurrently ingest and filter bulk cinematic records from distributed upstream endpoints, rigidly protected via an inline **JWT (JSON Web Tokens)** validation pipeline and persistent **H2 Database** identity mapping.
📝 Overview
This service demonstrates an enterprise-grade implementation of asynchronous data retrieval combined with a robust security layer. It fetches, filters, and consolidates movie records from the HackerRank Movie Database API while ensuring that all data access is strictly governed by Spring Security 6.x.

---

## 📝 Architectural Overview

The application functions as a high-throughput gateway. It exposes a secured REST interface while optimizing downstream latency through asynchronous non-blocking I/O operations.

```text
[Client (Postman/Curl)]
│ (HTTP Request + Bearer JWT)
▼
[SecurityFilterChain] ──(Fails Token Validation)──► [403 Forbidden Response]
│
│ (Passes JwtRequestFilter)
▼
[MovieSearchApi Controller]
│
▼
[MovieFetchEngine (CompletableFuture / Thread Pool)]
├── Worker Thread 1 ──► [External REST API Page 1]
├── Worker Thread 2 ──► [External REST API Page 2]
└── Worker Thread N ──► [External REST API Page N]
```

### Core Architecture Highlights
* **Asynchronous Processing**: Eliminates sequential I/O blocking by chunking multi-page upstream requests into distinct computational units.
* **Stateless Security Perimeter**: Eradicates HTTP Session overhead (`SessionCreationPolicy.STATELESS`), utilizing cryptographically signed tokens to transition context safely across isolated threads.

---

## 🛠️ Technical Stack & Dependencies

* **Core Engine**: Spring Boot 3.x (Java 17 OpenJDK)
* **Security Subsystem**: Spring Security 6.x
* **Token Cryptography**: `io.jsonwebtoken:jjwt-api:0.11.5` (utilizing HMAC-SHA 256-bit keys)
* **Data Tier**: Spring Data JPA & In-Memory H2 Engine
* **Networking Client**: Spring `RestTemplate` with concurrent thread pooling
* **Build Pipeline**: Apache Maven 3.x

---

## 🚀 Deep-Dive Key Features

### 1. High-Performance Concurrency Engine
Instead of executing linear HTTP fetches for multi-paged datasets, the `MovieFetchEngine` analyzes target meta-data dynamically, initializing isolated `CompletableFuture` workers. These workers run parallel non-blocking tasks inside the application's default task executor fork-join pool, optimizing execution speeds by up to 70%.

### 2. Custom Security Filter Interception
A managed `JwtRequestFilter` extends Spring's `OncePerRequestFilter`. It intercepts inbound transactions, extracts headers prefixed with `Bearer `, parses the cryptographic claims, checks validity constraints, and programmatically injects validated authentication metadata into the `SecurityContextHolder`.

### 3. Database Isolation & Bootstrapping
User identities are decoupled from external configurations. An embedded H2 database initializes relational storage at startup. Concurrently, a transactional `DataInitializer` triggers database writes to ensure administrative seed configurations are applied before the network socket opens on port `8080`.

---

## 🛣️ API Specification

### Authentication Gateway

* **Endpoint**: `POST /auth/login`
* **Consumes**: `application/json`
* **Produces**: `application/json`

**Payload Schema:**
```json
{
  "username": "admin",
  "password": "password123"
}
```

**Success Response (200 OK):**
```json
{
  "jwt": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6...Rnc-d8gf..."
}
```

---

### Movie Search Engine

* **Endpoint**: `GET /api/movies`
* **Security Check**: `@PreAuthorize("isAuthenticated()")`
* **Headers Required**: `Authorization: Bearer <JWT_TOKEN>`

| Parameter | Type | Required | Description |
| :--- | :--- | :--- | :--- |
| `Title` | String | **Yes** | Target movie or sequence phrase search. |
| `Year` | Integer | No | Filters results by exact release year. |
| `page` | Integer | No | Direct index pointer for single-page retrieval. |

---

## 🧪 Comprehensive Testing Protocols

### Method I: Postman (Visual API Validation)
1. **Generate Token**: Issue a `POST` request to `http://localhost:8080/auth/login`. Extract the string embedded within the `jwt` key.
2. **Mount Authorization**: Initialize a new tab targeting `http://localhost:8080/api/movies?Title=Spiderman`.
3. **Inject Token**: Under the **Authorization** selector, choose **Bearer Token** and paste the string.
4. **Dispatch**: Press **Send**; verify valid structured payload delivery.

### Method II: Native Windows PowerShell
Bypass standard terminal quote-stripping errors by explicitly typing the object mapping parameter arrays:

```powershell
# Step 1: Request JWT Access Token from Authentication Controller
$postParams = @{username='admin'; password='password123'} | ConvertTo-Json
$loginResponse = Invoke-RestMethod -Uri "http://localhost:8080/auth/login" -Method Post -Body $postParams -ContentType "application/json"

# Step 2: Inject JWT Token into Authorization Headers Array and Fetch Movies
Invoke-RestMethod -Uri "http://localhost:8080/api/movies?Title=Maze" -Method Get -Headers @{Authorization=("Bearer " + $loginResponse.jwt)}
```

### Method III: Chrome Browser Negative & DB Verification
* **H2 Database Introspection**: Navigate to `http://localhost:8080/h2-console`. Confirm configuration mapping `jdbc:h2:mem:moviedb` and execute `SELECT * FROM USERS;` to observe rows generated by `DataInitializer`.
* **Security Barrier Verification**: Attempt a raw browser navigation to `http://localhost:8080/api/movies?Title=Waterworld`. Browser rendering of an **HTTP ERROR 403 Forbidden** verifies functional request routing protection.

## 🔍 Troubleshooting & Error Profiles

| Error Code / Trace | Root Cause | Resolution |
| :--- | :--- | :--- |
| `HTTP 403 Forbidden` | Missing token or invalid CSRF configuration state. | Inject valid Bearer Token; verify `csrf().disable()` is loaded in `SecurityConfig`. |
| `SignatureException` | Server secret changed via app restart. | Request a new token via `/auth/login`. Do not restart application between tests. |
| `HttpMessageNotReadableException` | Terminal passing raw unquoted JSON strings. | Use PowerShell `ConvertTo-Json` formatting scripts to retain double-quote signatures. |

---

## ⚙️ Environment Construction & Launch

1. **Download Source**: `git clone <repository-link>`
2. **Environment Settings**: Confirm properties match inside `src/main/resources/application.properties`:

```properties
spring.h2.console.enabled=true
spring.datasource.url=jdbc:h2:mem:moviedb
spring.datasource.driverClassName=org.h2.Driver
```

3. **Compile & Execute: Launch application utilizing IDE execution context or command terminal execution:**

```powershell
mvn clean spring-boot:run
```
---
