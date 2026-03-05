# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /build

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the jar from builder stage
COPY --from=builder /build/target/exam-system-*.jar app.jar

# Expose port (default Spring Boot port)
EXPOSE 7860


# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
