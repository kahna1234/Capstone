# Running E-commerce Microservices with Podman

This guide explains how to build and run all the e-commerce microservices using Podman containers.

## Prerequisites

1. **Podman**: Install Podman on your system
   - Windows: https://podman.io/getting-started/
   - macOS: `brew install podman`
   - Linux: Follow your distribution's package manager

2. **Podman Compose**: Install podman-compose for easier orchestration
   ```bash
   pip install podman-compose
   ```
   Or use the built-in `podman compose` command (Podman 4.0+)

3. **Java 17/21**: Required for building the Spring Boot applications
4. **Maven**: Required for building the JAR files

## Services Overview

The application consists of the following services:

| Service | Port | Description |
|---------|------|-------------|
| Frontend | 3000 | React/Vue.js frontend served by Nginx |
| Service Discovery | 8761 | Eureka service registry |
| API Gateway | 8088 | Spring Cloud Gateway |
| User Auth Service | 8082 | User authentication and authorization |
| Product Service | 8084 | Product catalog management |
| Order Service | 8083 | Order processing |
| Payment Service | 8085 | Payment processing |
| Email Service | 8081 | Email notifications |
| MySQL | 3307 | Database server |
| Kafka | 9092 | Message broker |
| Zookeeper | 2181 | Kafka coordination |

## Quick Start

### Option 1: Using the Automated Script

The easiest way to get everything running is to use the provided PowerShell script:

```powershell
.\build-and-run-podman.ps1
```

This script will:
1. Build all Spring Boot JAR files
2. Build Docker images for each service
3. Start all services using podman-compose
4. Show the status and access URLs

### Option 2: Manual Steps

#### Step 1: Build All Services

```powershell
# Build each Spring Boot service
$services = @("ServiceDiscovery", "APIGateway", "ProductCatalogService", "OrderService", "PaymentService", "UserAuthService", "EmailService")

foreach ($service in $services) {
    Set-Location $service
    mvn clean package -DskipTests
    podman build -t localhost/${service.ToLower()}:latest .
    Set-Location ..
}

# Build frontend
Set-Location frontend
podman build -t localhost/frontend:latest .
Set-Location ..
```

#### Step 2: Start All Services

```powershell
# Using podman-compose
podman-compose -f podman-compose.yml up -d

# Or using built-in podman compose (Podman 4.0+)
podman compose -f podman-compose.yml up -d
```

## Managing Services

### View Running Containers
```bash
podman ps
```

### View Logs
```bash
# View logs for a specific service
podman logs service-discovery
podman logs api-gateway
podman logs frontend

# Follow logs in real-time
podman logs -f service-discovery
```

### Stop All Services
```bash
podman-compose -f podman-compose.yml down
```

### Restart Services
```bash
podman-compose -f podman-compose.yml restart
```

### Rebuild a Single Service
```bash
# Rebuild and restart a specific service
Set-Location OrderService
mvn clean package -DskipTests
podman build -t localhost/orderservice:latest .
Set-Location ..

podman-compose -f podman-compose.yml up -d --force-recreate order-service
```

## Accessing Services

Once all services are running, you can access them at:

- **Frontend**: http://localhost:3000
- **Service Discovery**: http://localhost:8761
- **API Gateway**: http://localhost:8088
- **User Auth Service**: http://localhost:8082
- **Product Service**: http://localhost:8084
- **Order Service**: http://localhost:8083
- **Payment Service**: http://localhost:8085
- **Email Service**: http://localhost:8081

## Database Access

- **MySQL**: localhost:3307 (no password)
- **Kafka**: localhost:9092

## Troubleshooting

### Common Issues

1. **Port Already in Use**
   ```bash
   # Check what's using the port
   netstat -ano | findstr :8088
   
   # Stop the conflicting service or change the port in podman-compose.yml
   ```

2. **Build Failures**
   - Ensure Maven is installed and in PATH
   - Check Java version (services use Java 17 or 21)
   - Verify all dependencies are available

3. **Container Startup Issues**
   ```bash
   # Check container logs
   podman logs <container-name>
   
   # Check container status
   podman ps -a
   ```

4. **Network Issues**
   - Ensure `host.docker.internal` resolves correctly
   - Check firewall settings for the required ports

### Health Checks

Some services include health checks. You can monitor them:

```bash
# Check MySQL health
podman exec mysql mysqladmin ping -h localhost -u root

# Check service registration
curl http://localhost:8761/eureka/apps
```

## Development Workflow

For development, you might want to run services individually:

```bash
# Start only the infrastructure services
podman-compose -f podman-compose.yml up -d mysql zookeeper kafka

# Start a single service
podman-compose -f podman-compose.yml up -d service-discovery

# Run a service locally (for debugging)
Set-Location APIGateway
mvn spring-boot:run
```

## Production Considerations

For production deployment:

1. **Security**: Remove `extra_hosts` and configure proper networking
2. **Resources**: Set memory and CPU limits in podman-compose.yml
3. **Persistence**: Configure proper volume mounts for data persistence
4. **Monitoring**: Add health checks and monitoring endpoints
5. **Secrets**: Use proper secret management instead of environment variables

## File Structure

```
Capstone/
|-- APIGateway/                 # API Gateway service
|   |-- Dockerfile
|   |-- pom.xml
|   `-- target/*.jar
|-- ServiceDiscovery/           # Eureka server
|   |-- Dockerfile
|   |-- pom.xml
|   `-- target/*.jar
|-- UserAuthService/           # User authentication
|   |-- Dockerfile
|   |-- pom.xml
|   `-- target/*.jar
|-- ProductCatalogService/     # Product catalog
|   |-- Dockerfile
|   |-- pom.xml
|   `-- target/*.jar
|-- OrderService/              # Order processing
|   |-- Dockerfile
|   |-- pom.xml
|   `-- target/*.jar
|-- PaymentService/            # Payment processing
|   |-- Dockerfile
|   |-- pom.xml
|   `-- target/*.jar
|-- EmailService/              # Email notifications
|   |-- Dockerfile
|   |-- pom.xml
|   `-- target/*.jar
|-- frontend/                  # Frontend application
|   |-- Dockerfile
|   |-- nginx.conf
|   |-- index.html
|   |-- css/
|   `-- js/
|-- podman-compose.yml         # Podman orchestration
|-- build-and-run-podman.ps1   # Automated build script
`-- PODMAN_SETUP.md           # This documentation
```

## Support

If you encounter issues:

1. Check the container logs for error messages
2. Verify all prerequisites are installed
3. Ensure ports are not already in use
4. Check network connectivity between containers

For more detailed troubleshooting, refer to the individual service documentation.
