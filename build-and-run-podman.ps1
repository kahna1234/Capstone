#!/usr/bin/env pwsh

# Build and Run Script for Podman
# This script builds all Docker images and starts the services with Podman

Write-Host "=== Building and Starting Services with Podman ===" -ForegroundColor Green

# Function to check if Podman is installed
function Test-PodmanInstalled {
    try {
        podman version | Out-Null
        return $true
    }
    catch {
        Write-Host "Podman is not installed or not in PATH" -ForegroundColor Red
        Write-Host "Please install Podman first: https://podman.io/getting-started/" -ForegroundColor Yellow
        return $false
    }
}

# Function to build all services
function Build-Services {
    Write-Host "Building all services..." -ForegroundColor Cyan
    
    $services = @("ServiceDiscovery", "APIGateway", "ProductCatalogService", "OrderService", "PaymentService", "UserAuthService", "EmailService", "CartService", "InventoryService")
    
    foreach ($service in $services) {
        Write-Host "Building $service..." -ForegroundColor Yellow
        
        # Check if the service directory exists
        if (Test-Path $service) {
            # Build the JAR file first
            Set-Location $service
            Write-Host "  Building JAR for $service..." -ForegroundColor Gray
            mvn clean package -DskipTests
            
            if ($LASTEXITCODE -ne 0) {
                Write-Host "  Failed to build JAR for $service" -ForegroundColor Red
                Set-Location ..
                continue
            }
            
            # Build Docker image with Podman
            Write-Host "  Building Docker image for $service..." -ForegroundColor Gray
            $imageName = $service.ToLower().Replace("service", "")
            podman build -t localhost/${imageName}:latest .
            
            if ($LASTEXITCODE -ne 0) {
                Write-Host "  Failed to build Docker image for $service" -ForegroundColor Red
            } else {
                Write-Host "  Successfully built $service as localhost/${imageName}:latest" -ForegroundColor Green
            }
            
            Set-Location ..
        } else {
            Write-Host "  Service directory $service not found" -ForegroundColor Red
        }
    }
    
    # Build frontend
    Write-Host "Building frontend..." -ForegroundColor Yellow
    if (Test-Path "frontend") {
        Set-Location frontend
        podman build -t localhost/frontend:latest .
        
        if ($LASTEXITCODE -ne 0) {
            Write-Host "Failed to build frontend" -ForegroundColor Red
        } else {
            Write-Host "Successfully built frontend" -ForegroundColor Green
        }
        Set-Location ..
    } else {
        Write-Host "Frontend directory not found" -ForegroundColor Red
    }
}

# Function to start services with Podman Compose
function Start-Services {
    Write-Host "Starting services with Podman Compose..." -ForegroundColor Cyan
    
    # Use docker-compose.yml with podman compose
    Write-Host "Using podman compose with docker-compose.yml..." -ForegroundColor Gray
    podman compose -f docker-compose.yml up -d
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Failed to start services" -ForegroundColor Red
        return $false
    } else {
        Write-Host "Services started successfully!" -ForegroundColor Green
        return $true
    }
}

# Function to show service status
function Show-ServiceStatus {
    Write-Host "=== Service Status ===" -ForegroundColor Cyan
    podman ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
}

# Main execution
if (-not (Test-PodmanInstalled)) {
    exit 1
}

# Build all services
Build-Services

# Start services
if (Start-Services) {
    # Wait a moment and show status
    Start-Sleep -Seconds 5
    Show-ServiceStatus
    
    Write-Host "`n=== Services Information ===" -ForegroundColor Green
    Write-Host "Frontend: http://localhost:3000" -ForegroundColor White
    Write-Host "Service Discovery: http://localhost:8761" -ForegroundColor White
    Write-Host "API Gateway: http://localhost:8088" -ForegroundColor White
    Write-Host "Product Service: http://localhost:8084" -ForegroundColor White
    Write-Host "Order Service: http://localhost:8083" -ForegroundColor White
    Write-Host "Payment Service: http://localhost:8085" -ForegroundColor White
    Write-Host "User Auth Service: http://localhost:8082" -ForegroundColor White
    Write-Host "Email Service: http://localhost:8081" -ForegroundColor White
    Write-Host "MySQL: localhost:3307" -ForegroundColor White
    Write-Host "Kafka: localhost:9092" -ForegroundColor White
    
    Write-Host "`n=== Useful Commands ===" -ForegroundColor Cyan
    Write-Host "View logs: podman logs <container-name>" -ForegroundColor Gray
    Write-Host "Stop all: podman-compose -f podman-compose.yml down" -ForegroundColor Gray
    Write-Host "Restart: podman-compose -f podman-compose.yml restart" -ForegroundColor Gray
} else {
    Write-Host "Failed to start services. Check the logs above." -ForegroundColor Red
}
