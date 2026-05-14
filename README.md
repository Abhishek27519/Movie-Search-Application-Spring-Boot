Movie Search Service Application (Secured)
A high-performance, stateless Spring Boot application that aggregates movie data from external APIs using concurrent processing, secured via JWT (JSON Web Tokens) and persistent H2 Database authentication.

📝 Overview
This service demonstrates an enterprise-grade implementation of asynchronous data retrieval combined with a robust security layer. It fetches, filters, and consolidates movie records from the HackerRank Movie Database API while ensuring that all data access is strictly governed by Spring Security 6.x.

🚀 Key Features
Concurrent Data Aggregation: Leverages Java's CompletableFuture and a custom-configured RestTemplate to fetch multiple pages of data simultaneously, significantly reducing I/O latency.

Stateless JWT Authentication: Implements a custom security filter chain to validate identity from Bearer tokens, ensuring the application remains scalable and stateless.

Persistent Identity Management: Utilizes an in-memory H2 Database for storing user credentials, allowing for verifiable authentication logic during the application lifecycle.

Method-Level Security: Employs @PreAuthorize("isAuthenticated()") annotations to guard REST endpoints at the controller level.

Automated Data Initialization: Features a DataInitializer component that bootstraps a default administrative user into H2 upon startup for immediate testing.

🛠 Technical Stack
Framework: Spring Boot 3.x

Security: Spring Security 6.x & io.jsonwebtoken (jjwt)

Database: H2 (In-memory)

Language: Java 17+

Concurrency: CompletableFuture API

Build Tool: Maven

🛣 API Endpoints
1. Authentication (Public)
   URL: POST /auth/login

Purpose: Validates credentials and returns a signed JWT.
Body:

JSON
{
"username": "admin",
"password": "password123"
}
2. Secured Movie Search (Requires JWT)
   URL: GET /api/movies

Parameters: Title (Required), Year (Optional), page (Optional).

Header: Authorization: Bearer <JWT_TOKEN>

🧪 Comprehensive Testing Process
I. Postman (Standard API Testing)
Login: Send a POST to /auth/login. Copy the jwt string from the response.

Authorize: Open a GET request to /api/movies?Title=Maze.

Setup Header: In the Authorization tab, select Bearer Token and paste your JWT.

Execute: Hit Send to receive the concurrent search results.

II. PowerShell (Dev-Ops / Terminal Verification)
Utilize native PowerShell commands to handle JSON object mapping and bypass quote-escaping issues:

PowerShell
# 1. Generate Token
$postParams = @{username='admin'; password='password123'} | ConvertTo-Json
$loginResponse = Invoke-RestMethod -Uri "http://localhost:8080/auth/login" -Method Post -Body $postParams -ContentType "application/json"

# 2. Access Secured Resource
Invoke-RestMethod -Uri "http://localhost:8080/api/movies?Title=Harry" -Method Get -Headers @{Authorization=("Bearer " + $loginResponse.jwt)}
III. Chrome Browser (Security & DB Verification)
H2 Console: Access http://localhost:8080/h2-console (JDBC URL: jdbc:h2:mem:moviedb) to verify the USERS table.

Negative Testing: Visit http://localhost:8080/api/movies directly in Chrome. An HTTP 403 Forbidden error confirms the security filter is successfully protecting the resource.

⚙️ Setup & Installation
Clone: git clone <repo-url>

Config: Ensure src/main/resources/application.properties includes spring.h2.console.enabled=true.

Run: Launch via IntelliJ or ./mvnw spring-boot:run.

Initial Credentials: Username: admin | Password: password123.