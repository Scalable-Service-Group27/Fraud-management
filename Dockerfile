# Stage 1: Build the application using Maven
FROM maven:3.9.5-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the full source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application using a lightweight JDK image
FROM eclipse-temurin:17-jdk-alpine

# Create a non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Set working directory
WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/Fraud-Management-0.0.1-SNAPSHOT.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8761

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
