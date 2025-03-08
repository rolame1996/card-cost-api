
# Card Cost API

This is a **REST API** developed in **Java 17** with **Spring Boot 3.4.3**, designed to calculate the processing cost of bank cards.  
The application is **dockerized** and includes **Swagger** for interactive documentation.  
It uses an **H2 in-memory database** for local testing.

## Technologies Used
- **Java 17**
- **Spring Boot 3.4.3**
- **Spring Web** (for REST API)
- **Spring Data JPA** (for persistence)
- **H2 Database** (for local testing)
- **Mockito & JUnit 5** (for testing)
- **Swagger** (for API documentation)
- **Docker** (for deployment)

## Run with Docker

Build Spring boot image (From the position of the Dockerfile)
- docker build -t card-cost-api .

Run backend service and host it on localhost port 8080
- docker run -p --dns=8.8.8.8 --dns=8.8.4.4 8080:8080 card-cost-api

## API Documentation with Swagger

http://localhost:8080/swagger-ui/index.html

## Testing

Use the postman collection for testing local.

Run the tests locally
- ./gradlew test



