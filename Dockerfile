FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

COPY gradlew .
COPY gradle ./gradle
COPY build.gradle settings.gradle ./
# Give the gradlew script files executable permission
RUN chmod +x gradlew

COPY src ./src

# bootJar builds a fat jar (with all the dependencies) | --no-daemon disables background speed-up processes to ensure Gradle runs once and exits cleanly
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]