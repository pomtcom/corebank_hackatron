# === Stage 1: Build the application ===
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml and download dependencies first (for layer caching)
COPY pom.xml .
COPY src ./src

# Build the project
RUN mvn clean package -DskipTests

# === Stage 2: Run the application ===
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy only the jar from the previous stage
COPY --from=build /app/target/corebank-hackatron-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
