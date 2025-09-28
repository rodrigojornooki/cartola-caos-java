# Multi-stage build for better efficiency
FROM openjdk:17-jdk-slim AS build

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml first for better caching
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Give execution permission to mvnw
RUN chmod +x ./mvnw

# Copy source code
COPY src ./src

# Build the application (skip dependency resolve step for better reliability)
RUN ./mvnw clean package -DskipTests

# Production stage
FROM openjdk:17-jdk-slim AS production

# Install curl for health checks (optional)
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy startup script and built JAR
COPY startup.sh ./
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar ./app.jar

# Make startup script executable
RUN chmod +x startup.sh

# Create non-root user for security
RUN addgroup --system --gid 1001 appuser && \
    adduser --system --uid 1001 --gid 1001 appuser && \
    chown -R appuser:appuser /app

USER appuser

# Expose port
EXPOSE 8080

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=production
ENV SERVER_PORT=8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application using the startup script
CMD ["./startup.sh"]