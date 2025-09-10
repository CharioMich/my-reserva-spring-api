# my-reserva-spring-api
An alternative Java Spring Boot RESTful API for myReserva full-stack personal project

---
Mirroring the primary project's api developed with Node.js, Express.js and Typescript. <br> 
Primary API Repo: https://github.com/CharioMich/my-reserva-api <br>
Front-end Repo: https://github.com/CharioMich/myReserva-app

## Description
A Spring Boot application built with a service-oriented, layered architecture.
It features a robust authentication system utilizing Spring Security (UserDetails and related components) with refresh token support.


The application provides:

User features: CRUD APIs, authentication, and reservation management.

Admin features: Reservation monitoring with filtering by date and user information.

### Requirements
- mySQL Database
- Java 21+ 
- Git

## Run Instructions

1. Clone the repo ``` git clone git@github.com:CharioMich/my-reserva-spring-api.git ``` (SSH)


2. In ``` src/main/resources/application.properties ``` set the spring profile to dev 
```java 
   spring.profiles.active=dev  
```
3. Go to ``` src/main/resources/application-dev.properties ``` and replace the fields containing '!' such as ``` !YourDataBaseName! ```
with your corresponding database name, credentials and jwt secret key.


4. Build the project and run ``` ./gradlew bootRun ```

The project should be running on ``` http://localhost:8080/api ```
