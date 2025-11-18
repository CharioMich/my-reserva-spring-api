# my-reserva-spring-api
An alternative Java Spring Boot RESTful API for myReserva full-stack personal project

---
Mirroring the primary project's api developed with Node.js, Express.js and Typescript. <br> 
Primary API Repo: https://github.com/CharioMich/my-reserva-api <br>
Front-end Repo: https://github.com/CharioMich/myReserva-app

## Description
A Spring Boot application built with a service-oriented, layered architecture.
It provides a Users CRUD API and reservations handling. Admins restricted from an admin whitelist can monitor user data and  
reservations filtered by date.
### App Features:
- Custom User Details Implementation
- Spring Security Implementation including filter chain and cors check
- Jwt Authentication including request filter
- Refresh Token Functionality
- Admin Email Address Whitelist and Role checks
- Global Error Handler and Custom Exceptions
- Pagination for GET Users endpoint

### Requirements
- mySQL Database
- Java 21+ 
- Git

## Run Instructions

1. Clone the repo ``` git clone git@github.com:CharioMich/my-reserva-spring-api.git ``` (SSH)


2. Create a ```.env``` file, paste the following and update it with your credentials:
```declarative
# MySQL credentials
MYSQL_ROOT_PASSWORD=root
MYSQL_DATABASE=myreserva
MYSQL_USER=myuser
MYSQL_PASSWORD=mypassword
```


3. In ``` src/main/resources/application.properties ``` set the spring profile to dev 
```java 
   spring.profiles.active=dev  
```

4. Go to ``` src/main/resources/application-dev.properties ``` and replace the fields containing '!' such as ``` !YourDataBaseName! ```
with your corresponding database name, credentials and jwt secret key. These are the fallback values in case
```.env``` the system is unable to retrieve the environmental variables from ```.env``` file.
   Optionally, add email addresses to admin whitelist. ``` admin.whitelist=admin@aueb.gr, ... ```


5. Run the project locally with:
```declarative
./gradlew bootRun
```

6. To run with docker:
```declarative
docker compose up --build
```

The project should be running on ``` http://localhost:8080/api ```


Browse to http://localhost:8080/api/swagger-ui/index.html for OpenAPI endpoint documentation. (documentation not complete)

7. (Docker clean up) Stop & remove containers and delete the database data:
```declarative
docker compose down -v
```

---
## Postman
A Postman collection and environment to test the API are included under the ``` src/main/resources/postman ``` directory.
#### How to Use:
1. Open Postman.
2.  Import the collection: postman/myReservaSpring.postman_collection.json.
3.  Import the environment: postman/myReservaSpring.postman_environment.json.
4.  Select the environment (adjust variables if needed) and run requests.