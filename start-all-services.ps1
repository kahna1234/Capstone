#!/usr/bin/env pwsh

$ErrorActionPreference = "Stop"
$networkName = "capstone-network"

function Remove-ExistingContainer {
    param([string]$Name)

    $existing = podman ps -a --filter "name=^$Name$" --format "{{.Names}}"
    if ($existing -eq $Name) {
        podman rm -f $Name | Out-Null
    }
}

function Wait-ForHttp {
    param([string]$Url, [int]$TimeoutSeconds = 120)

    $deadline = (Get-Date).AddSeconds($TimeoutSeconds)
    do {
        try {
            Invoke-WebRequest -Uri $Url -UseBasicParsing -TimeoutSec 3 | Out-Null
            return
        } catch {
            Start-Sleep -Seconds 3
        }
    } while ((Get-Date) -lt $deadline)

    throw "Timed out waiting for $Url"
}

function Wait-ForMysql {
    param([int]$TimeoutSeconds = 120)

    $deadline = (Get-Date).AddSeconds($TimeoutSeconds)
    do {
        podman exec mysql mysqladmin ping -h localhost -u root --silent *> $null
        if ($LASTEXITCODE -eq 0) {
            return
        }
        Start-Sleep -Seconds 3
    } while ((Get-Date) -lt $deadline)

    throw "Timed out waiting for MySQL"
}

$network = podman network ls --filter "name=^$networkName$" --format "{{.Name}}"
if ($network -ne $networkName) {
    podman network create $networkName | Out-Null
}

@(
    "api-gateway",
    "email-service",
    "inventory-service",
    "order-service",
    "payment-service",
    "product-service",
    "service-discovery",
    "user-auth-service",
    "kafka",
    "zookeeper",
    "mysql"
) | ForEach-Object { Remove-ExistingContainer -Name $_ }

Write-Host "Starting MySQL..." -ForegroundColor Yellow
podman run -d --name mysql --network $networkName -p 3307:3306 -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -v "${PWD}/docker/mysql/init.sql:/docker-entrypoint-initdb.d/init.sql:ro" docker.io/library/mysql:8.4 | Out-Null
Wait-ForMysql

Write-Host "Starting Zookeeper and Kafka..." -ForegroundColor Yellow
podman run -d --name zookeeper --network $networkName -p 2181:2181 -e ZOOKEEPER_CLIENT_PORT=2181 -e ZOOKEEPER_TICK_TIME=2000 docker.io/confluentinc/cp-zookeeper:7.5.0 | Out-Null
podman run -d --name kafka --network $networkName -p 9092:9092 -e KAFKA_BROKER_ID=1 -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://kafka:9092 -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 docker.io/confluentinc/cp-kafka:7.5.0 | Out-Null

Write-Host "Starting Service Discovery..." -ForegroundColor Yellow
podman run -d --name service-discovery --network $networkName -p 8761:8761 localhost/servicediscovery:latest | Out-Null
Wait-ForHttp -Url "http://localhost:8761"

$eurekaUrl = "http://service-discovery:8761/eureka/"
$commonEurekaEnv = @(
    "-e", "EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=$eurekaUrl",
    "-e", "EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=$eurekaUrl"
)

Write-Host "Starting application services..." -ForegroundColor Yellow
podman run -d --name api-gateway --network $networkName -p 8088:8088 @commonEurekaEnv localhost/apigateway:latest | Out-Null
podman run -d --name email-service --network $networkName -p 8081:8081 @commonEurekaEnv -e SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092 localhost/emailservice:latest | Out-Null
podman run -d --name inventory-service --network $networkName -p 8086:8086 @commonEurekaEnv -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/inventoryservice -e SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092 localhost/inventoryservice:latest | Out-Null
podman run -d --name user-auth-service --network $networkName -p 8082:8082 @commonEurekaEnv -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/user_auth_service -e SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092 localhost/userauthservice:latest | Out-Null
podman run -d --name order-service --network $networkName -p 8083:8083 @commonEurekaEnv -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/orderservice -e SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092 localhost/orderservice:latest | Out-Null
podman run -d --name product-service --network $networkName -p 8084:8084 @commonEurekaEnv -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/product_catalog_service -e SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092 localhost/productcatalogservice:latest | Out-Null
podman run -d --name payment-service --network $networkName -p 8085:8085 @commonEurekaEnv -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/paymentservice -e SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092 localhost/paymentservice:latest | Out-Null

Start-Sleep -Seconds 15
podman ps --format "table {{.Names}}\t{{.Ports}}\t{{.Status}}"
exit 0

# Start all services in the correct order
Write-Host "Starting all services..." -ForegroundColor Green

# 1. Start Service Discovery first
Write-Host "`n1. Starting Service Discovery..." -ForegroundColor Yellow
podman run -d `
  --name service-discovery `
  -p 8761:8761 `
  --add-host host.docker.internal:host-gateway `
  localhost/servicediscovery:latest

Write-Host "Service Discovery started. Waiting for it to be ready..." -ForegroundColor Cyan
Start-Sleep -Seconds 5

# 2. Start API Gateway
Write-Host "`n2. Starting API Gateway..." -ForegroundColor Yellow
podman run -d `
  --name api-gateway `
  -p 8088:8088 `
  -e EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://service-discovery:8761/eureka/ `
  --add-host host.docker.internal:host-gateway `
  localhost/apigateway:latest

# 3. Start Email Service
Write-Host "`n3. Starting Email Service..." -ForegroundColor Yellow
podman run -d `
  --name email-service `
  -p 8081:8081 `
  -e EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://service-discovery:8761/eureka/ `
  -e SPRING_KAFKA_BOOTSTRAP_SERVERS=host.docker.internal:9092 `
  --add-host host.docker.internal:host-gateway `
  localhost/emailservice:latest

# 4. Start User Auth Service
Write-Host "`n4. Starting User Auth Service..." -ForegroundColor Yellow
podman run -d `
  --name user-auth-service `
  -p 8082:8082 `
  -e EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://service-discovery:8761/eureka/ `
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/user_auth_service `
  -e SPRING_KAFKA_BOOTSTRAP_SERVERS=host.docker.internal:9092 `
  --add-host host.docker.internal:host-gateway `
  localhost/userauthservice:latest

# 5. Start Order Service
Write-Host "`n5. Starting Order Service..." -ForegroundColor Yellow
podman run -d `
  --name order-service `
  -p 8083:8083 `
  -e EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://service-discovery:8761/eureka/ `
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/orderservice `
  -e SPRING_KAFKA_BOOTSTRAP_SERVERS=host.docker.internal:9092 `
  --add-host host.docker.internal:host-gateway `
  localhost/orderservice:latest

# 6. Start Product Catalog Service
Write-Host "`n6. Starting Product Catalog Service..." -ForegroundColor Yellow
podman run -d `
  --name product-service `
  -p 8084:8084 `
  -e EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://service-discovery:8761/eureka/ `
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/product_catalog_service `
  -e SPRING_KAFKA_BOOTSTRAP_SERVERS=host.docker.internal:9092 `
  --add-host host.docker.internal:host-gateway `
  localhost/productcatalogservice:latest

# 7. Start Payment Service
Write-Host "`n7. Starting Payment Service..." -ForegroundColor Yellow
podman run -d `
  --name payment-service `
  -p 8085:8085 `
  -e EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://service-discovery:8761/eureka/ `
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/paymentservice `
  -e SPRING_KAFKA_BOOTSTRAP_SERVERS=host.docker.internal:9092 `
  --add-host host.docker.internal:host-gateway `
  localhost/paymentservice:latest

Write-Host "`n✅ All services started!" -ForegroundColor Green
Write-Host "`nWaiting for services to initialize..." -ForegroundColor Cyan
Start-Sleep -Seconds 10

# Display running containers
Write-Host "`n📦 Running Containers:" -ForegroundColor Green
podman ps --format "table {{.Names}}\t{{.Ports}}\t{{.Status}}"

Write-Host "`n📚 Service URLs:" -ForegroundColor Green
Write-Host "  Service Discovery: http://localhost:8761"
Write-Host "  API Gateway:       http://localhost:8088"
Write-Host "  Email Service:     http://localhost:8081"
Write-Host "  User Auth Service: http://localhost:8082"
Write-Host "  Order Service:     http://localhost:8083"
Write-Host "  Product Service:   http://localhost:8084"
Write-Host "  Payment Service:   http://localhost:8085"

Write-Host "`n💡 Commands:" -ForegroundColor Cyan
Write-Host "  View logs:    podman logs -f <container-name>"
Write-Host "  Stop all:     podman stop -a"
Write-Host "  Remove all:   podman rm -f <container-name>"

