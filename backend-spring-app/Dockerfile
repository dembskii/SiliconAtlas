# ============================================
# CPU Management System - Spring Boot Runtime
# Build JAR on host: ./gradlew build
# ============================================

FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy pre-built JAR from host build directory
# Build the JAR on your machine first: cd backend-spring-app && ./gradlew build
COPY build/libs/silicon-atlas-1.0.0-SNAPSHOT.jar app.jar

# Create application user for security
RUN addgroup -g 1001 appgroup && \
    adduser -S appuser -u 1001 -G appgroup && \
    chown -R appuser:appgroup /app

USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run application with docker profile 
ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-Dspring.profiles.active=docker", "-jar", "app.jar"]
