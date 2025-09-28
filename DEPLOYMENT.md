# Cartola Caos Java - Free Deployment Guide

This guide provides multiple options for deploying the Cartola Caos Java application for free.

## Overview

The Cartola Caos application is a Spring Boot 3.1.4 application with Java 17 that provides a "negative fantasy football" game based on the Brazilian Cartola FC platform.

## Prerequisites

- Java 17+
- PostgreSQL database
- Internet connection for external API calls

## Free Deployment Options

### Option 1: Render.com (Recommended)

Render.com provides free tier hosting with PostgreSQL database support.

#### Steps:

1. **Fork/Clone the repository** to your GitHub account

2. **Create a Render account** at [render.com](https://render.com)

3. **Deploy using render.yaml**:
   - Connect your GitHub repository to Render
   - The `render.yaml` file is already configured
   - Render will automatically create:
     - Web service for the Spring Boot application
     - PostgreSQL database service

4. **Environment Variables** (automatically configured):
   - `SPRING_PROFILES_ACTIVE=production`
   - `DATABASE_URL` (from PostgreSQL service)
   - `DB_USERNAME` (from PostgreSQL service)  
   - `DB_PASSWORD` (from PostgreSQL service)
   - `CARTOLA_TOKEN` (update with your own token)

5. **Access your application**:
   - Web app: `https://your-app-name.onrender.com`
   - API docs: `https://your-app-name.onrender.com/swagger-ui.html`
   - Health check: `https://your-app-name.onrender.com/actuator/health`

#### Render Free Tier Limitations:
- 750 hours/month runtime
- Service spins down after 15 minutes of inactivity
- Cold start delay (~30 seconds)
- PostgreSQL: 1GB storage, 1 month retention

### Option 2: Railway.app

Railway provides an excellent free tier with built-in PostgreSQL.

#### Steps:

1. **Create a Railway account** at [railway.app](https://railway.app)

2. **Deploy from GitHub**:
   - Connect your GitHub repository
   - Railway will detect the `railway.json` configuration
   - Add PostgreSQL database plugin

3. **Set Environment Variables**:
   ```
   SPRING_PROFILES_ACTIVE=production
   DATABASE_URL=${{Postgres.DATABASE_URL}}
   CARTOLA_TOKEN=your-cartola-token-here
   ```

4. **Deploy**:
   - Railway automatically builds using the Dockerfile
   - Application will be available at the provided URL

#### Railway Free Tier Limitations:
- $5 monthly credit
- No sleeping (stays active)
- Usage-based billing after credit exhaustion

### Option 3: Heroku (Limited Free Options)

Note: Heroku discontinued their free tier, but educational accounts may still have access.

#### Steps:

1. **Install Heroku CLI**
2. **Login to Heroku**: `heroku login`
3. **Create app**: `heroku create your-app-name`
4. **Add PostgreSQL**: `heroku addons:create heroku-postgresql:hobby-dev`
5. **Set environment variables**:
   ```bash
   heroku config:set SPRING_PROFILES_ACTIVE=production
   heroku config:set CARTOLA_TOKEN=your-token
   ```
6. **Deploy**: `git push heroku main`

## Local Development

### Running Locally

1. **Clone the repository**:
   ```bash
   git clone https://github.com/rodrigojornooki/cartola-caos-java.git
   cd cartola-caos-java
   ```

2. **Set up PostgreSQL**:
   - Install PostgreSQL locally
   - Create database `cartola_db`
   - Update `application.properties` with your credentials

3. **Run the application**:
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Access the application**:
   - Web app: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - Health check: http://localhost:8080/actuator/health

### Using Docker

1. **Build the image**:
   ```bash
   docker build -t cartola-caos-java .
   ```

2. **Run with Docker Compose** (create docker-compose.yml):
   ```yaml
   version: '3.8'
   services:
     app:
       build: .
       ports:
         - "8080:8080"
       environment:
         - SPRING_PROFILES_ACTIVE=production
         - DATABASE_URL=jdbc:postgresql://db:5432/cartola_db
         - DB_USERNAME=postgres
         - DB_PASSWORD=password
       depends_on:
         - db
     
     db:
       image: postgres:15
       environment:
         - POSTGRES_DB=cartola_db
         - POSTGRES_USER=postgres
         - POSTGRES_PASSWORD=password
       ports:
         - "5432:5432"
   ```

3. **Run**:
   ```bash
   docker-compose up
   ```

## Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_PROFILES_ACTIVE` | Spring profile | `production` |
| `SERVER_PORT` | Application port | `8080` |
| `DATABASE_URL` | PostgreSQL connection URL | Required |
| `DB_USERNAME` | Database username | Required |
| `DB_PASSWORD` | Database password | Required |
| `CARTOLA_TOKEN` | Cartola FC API token | Required |
| `ALLOWED_ORIGINS` | CORS allowed origins | `*` |

### Important Notes

1. **API Token**: Update the `CARTOLA_TOKEN` with your own token from Cartola FC
2. **Database**: The application uses PostgreSQL with automatic schema updates
3. **External API**: The app integrates with Cartola FC's official API
4. **Scheduled Tasks**: The application runs scheduled tasks every 5 minutes to update player scores

## API Endpoints

- **Swagger UI**: `/swagger-ui.html`
- **API Documentation**: `/api-docs`
- **Health Check**: `/actuator/health`
- **Application Info**: `/actuator/info`

## Troubleshooting

### Common Issues

1. **Cold Start Delays**: Free tier services may have cold start delays
2. **Database Connections**: Ensure DATABASE_URL is properly configured
3. **API Token Expiry**: Update CARTOLA_TOKEN if requests fail
4. **Memory Limits**: Free tiers have memory constraints

### Monitoring

- Use the health check endpoint for monitoring
- Check application logs in your deployment platform
- Monitor database usage to stay within free tier limits

## Support

For issues with the application:
- Check the GitHub repository issues
- Review the application logs
- Verify environment variables are set correctly

## License

This project follows the original repository's license terms.