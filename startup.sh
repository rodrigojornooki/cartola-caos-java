#!/bin/bash

# Cartola Caos Java Startup Script
echo "Starting Cartola Caos Java Application..."

# Check if required environment variables are set
echo "Checking environment variables..."

if [ -z "$DATABASE_URL" ]; then
    echo "WARNING: DATABASE_URL not set, using default local PostgreSQL"
fi

if [ -z "$CARTOLA_TOKEN" ]; then
    echo "WARNING: CARTOLA_TOKEN not set, external API calls may fail"
fi

echo "Environment variables check completed."
echo "Starting Spring Boot application..."

# Start the application with optimized JVM settings for container deployment
exec java \
    -XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:+UseG1GC \
    -XX:+UseStringDeduplication \
    -Djava.security.egd=file:/dev/./urandom \
    -jar target/backend-0.0.1-SNAPSHOT.jar