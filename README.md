# Movie Search Service Application

A high-performance Spring Boot application that aggregates movie data from external APIs using concurrent processing.

## Overview
This project is a RESTful service designed to fetch, filter, and consolidate movie records from the HackerRank Movie Database API. It demonstrates a clean implementation of asynchronous data retrieval in a Java environment.

## Key Features
- **Concurrent Data Retrieval**: Leverages Java's `CompletableFuture` and `RestTemplate` to fetch multiple pages of data simultaneously, significantly reducing total response time.
- **Flexible Search API**: Implements a dual-method controller strategy that handles both bulk data aggregation and specific filtered queries (by Title, Year, and Page).
- **Clean Architecture**: Follows a modular design with a clear separation of concerns between the configuration, service (logic), and controller (interface) layers.

## Technical Stack
- **Framework**: Spring Boot
- **Build Tool**: Maven
- **Language**: Java
- **HTTP Client**: RestTemplate
- **Concurrency**: CompletableFuture

## API Endpoints

### 1. Fetch All Records
Retrieves every movie record available across all pages of the external API using multithreading.
- **URL**: `GET /api/movies`

### 2. Filtered Search
Search for specific movies using query parameters.
- **URL**: `GET /api/movies?Title={name}&Year={year}&page={number}`
- **Parameters**:
    - `Title` (Required for filtered search)
    - `Year` (Optional)
    - `page` (Optional)

## Project Components
- **MovieSearchApi**: REST Controller managing endpoint routing.
- **MovieFetchEngine**: Service layer containing the multithreading logic and API integration.
- **AppConfig**: Configuration class for Spring Bean management (RestTemplate).

## Setup
1. Clone the repository.
2. Run the application via IntelliJ IDEA or using `./mvnw spring-boot:run`.
3. Access the endpoints at `http://localhost:8080/api/movies`.
4. Also, access the endpoints with filters:
    - `http://localhost:8080/api/movies?Title=Time&Year=1995`
    - `http://localhost:8080/api/movies?Title=Maze&page=2`