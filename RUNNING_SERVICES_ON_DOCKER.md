# Running All Services on Docker/Podman

## Prerequisites

Before running the services, ensure the following are running on your host:

### 1. MySQL Database
You need to have MySQL running with the following databases created:
- `user_auth_service`
- `product_catalog_service`
- `orderservice`
- `paymentservice`

**Quick setup:**
```bash
# If using MySQL locally
mysql -u root -p
```

Then create databases:
```sql
CREATE DATABASE user_auth_service;
CREATE DATABASE product_catalog_service;
CREATE DATABASE orderservice;
CREATE DATABASE paymentservice;
```

### 2. Kafka & Zookeeper
Kafka and Zookeeper should be running for message queue functionality.

You can start them individually or check if they're already running:
```bash
podman ps | findstr kafka
podman ps | findstr zookeeper
```

## Running All Services

### Option 1: Using the PowerShell Script (Recommended)
```powershell
cd D:\Capstone
powershell -ExecutionPolicy Bypass -File start-all-services.ps1
```

This script will:
1. Start Service Discovery first
2. Wait for it to be ready
3. Start all other services with proper environment variables
4. Display running containers and service URLs

### Option 2: Manual Start
Start services in this order:

**1. Service Discovery**
```bash
podman run -d --name service-discovery -p 8761:8761 localhost/servicediscovery:latest
```

**Wait 5 seconds for Service Discovery to initialize**

**2. API Gateway**
```bash
podman run -d --name api-gateway -p 8088:8088 \
  -e EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://service-discovery:8761/eureka/ \
  localhost/apigateway:latest
```

**3. Email Service**
```bash
podman run -d --name email-service -p 8081:8081 \
  -e EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://service-discovery:8761/eureka/ \
  -e SPRING_KAFKA_BOOTSTRAP_SERVERS=host.docker.internal:9092 \
  localhost/emailservice:latest
```

**4. User Auth Service**
```bash
podman run -d --name user-auth-service -p 8082:8082 \
  -e EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://service-discovery:8761/eureka/ \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/user_auth_service \
  -e SPRING_KAFKA_BOOTSTRAP_SERVERS=host.docker.internal:9092 \
  localhost/userauthservice:latest
```

**5. Order Service**
```bash
podman run -d --name order-service -p 8083:8083 \
  -e EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://service-discovery:8761/eureka/ \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/orderservice \
  -e SPRING_KAFKA_BOOTSTRAP_SERVERS=host.docker.internal:9092 \
  localhost/orderservice:latest
```

**6. Product Catalog Service**
```bash
podman run -d --name product-service -p 8084:8084 \
  -e EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://service-discovery:8761/eureka/ \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/product_catalog_service \
  -e SPRING_KAFKA_BOOTSTRAP_SERVERS=host.docker.internal:9092 \
  localhost/productcatalogservice:latest
```

**7. Payment Service**
```bash
podman run -d --name payment-service -p 8085:8085 \
  -e EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://service-discovery:8761/eureka/ \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/paymentservice \
  -e SPRING_KAFKA_BOOTSTRAP_SERVERS=host.docker.internal:9092 \
  localhost/paymentservice:latest
```

## Service URLs

Once all services are running, access them at:

| Service | URL | Port |
|---------|-----|------|
| Service Discovery (Eureka) | http://localhost:8761 | 8761 |
| API Gateway | http://localhost:8088 | 8088 |
| Email Service | http://localhost:8081 | 8081 |
| User Auth Service | http://localhost:8082 | 8082 |
| Order Service | http://localhost:8083 | 8083 |
| Product Catalog Service | http://localhost:8084 | 8084 |
| Payment Service | http://localhost:8085 | 8085 |

## Useful Commands

### View Running Containers
```bash
podman ps
```

### View Container Logs
```bash
podman logs -f <container-name>
```

Examples:
```bash
podman logs -f service-discovery
podman logs -f api-gateway
podman logs -f user-auth-service
```

### Stop All Services
```bash
podman stop $(podman ps -q)
```

Or stop specific containers:
```bash
podman stop service-discovery api-gateway user-auth-service order-service product-service payment-service email-service
```

### Remove Containers
```bash
podman rm service-discovery api-gateway user-auth-service order-service product-service payment-service email-service
```

### Restart a Service
```bash
podman restart <container-name>
```

### Check Container Status
```bash
podman ps -a --format "table {{.Names}}\t{{.Status}}"
```

## Troubleshooting

### Service Fails to Start

Check logs:
```bash
podman logs <container-name> | Select-Object -Last 100
```

Common issues:
1. **Database connection failed** - Ensure MySQL is running and accessible
2. **Service Discovery not ready** - Wait longer before starting other services
3. **Port already in use** - Stop existing containers or use different ports
4. **Kafka not running** - Start Kafka and Zookeeper before services

### Container Exists Error

If you get "container name already in use" error:
```bash
podman rm -f <container-name>
```

### Network Issues

Services communicate via `host.docker.internal` which routes to the host machine. Ensure:
- MySQL is listening on `localhost:3306`
- Kafka is listening on `localhost:9092`
- Services can resolve `host.docker.internal` (should work by default)

## Docker Compose Alternative

If you want to use podman-compose (requires Python installation), you can use the podman-compose.yml file:

```bash
podman-compose -f podman-compose.yml up -d
```

This would automatically handle service dependencies and startup order.

## Network Details

All containers use the `--add-host host.docker.internal:host-gateway` flag to allow containers to communicate with services running on the host machine (MySQL, Kafka, etc.).

This is equivalent to Docker's native `host.docker.internal` support on Windows and macOS.

