#!/usr/bin/env pwsh

$ErrorActionPreference = "Stop"
$composeFile = Join-Path $PWD "podman-compose.yml"
$envFile = Join-Path $PWD ".env"

function Test-PodmanInstalled {
    try {
        podman version | Out-Null
        return $true
    } catch {
        return $false
    }
}

if (-not (Test-PodmanInstalled)) {
    throw "Podman is not installed or not available in PATH."
}

if (-not (Test-Path $composeFile)) {
    throw "Missing compose file at $composeFile"
}

if (-not (Test-Path $envFile)) {
    throw "Missing .env file at $envFile"
}

Write-Host "Starting Capstone stack with Podman Compose..." -ForegroundColor Green
Write-Host "Compose file: $composeFile" -ForegroundColor Gray

podman compose -f $composeFile up -d
if ($LASTEXITCODE -ne 0) {
    throw "Podman Compose failed to start the Capstone stack."
}

Start-Sleep -Seconds 5

Write-Host "`nRunning containers:" -ForegroundColor Cyan
podman ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

Write-Host "`nService URLs:" -ForegroundColor Green
Write-Host "  Frontend:          http://localhost:3000"
Write-Host "  Service Discovery: http://localhost:8761"
Write-Host "  API Gateway:       http://localhost:8088"
Write-Host "  MySQL:             localhost:3307"
Write-Host "  Kafka:             localhost:9092"
Write-Host "  Elasticsearch:     http://localhost:9200"

Write-Host "`nUseful commands:" -ForegroundColor Cyan
Write-Host "  View payment logs: podman logs -f payment-service"
Write-Host "  View frontend logs: podman logs -f frontend"
Write-Host "  Stop stack:        podman compose -f podman-compose.yml down"