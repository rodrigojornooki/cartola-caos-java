# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml first for better caching
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Give execution permission to mvnw
RUN chmod +x ./mvnw

# Download dependencies
RUN ./mvnw dependency:resolve

# Copy source code and startup script
COPY src ./src
COPY startup.sh ./

# Build the application
RUN ./mvnw clean package -DskipTests

# Make startup script executable
RUN chmod +x startup.sh

# Expose port
EXPOSE 8080

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=production
ENV SERVER_PORT=8080

# Run the application using the startup script
CMD ["./startup.sh"]