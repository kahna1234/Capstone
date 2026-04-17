@echo off

echo ===============================
echo Starting Service Discovery (Eureka)
echo ===============================
start cmd /k "cd ServiceDiscovery && mvn spring-boot:run"

timeout /t 8

echo ===============================
echo Starting Microservices
echo ===============================

:: UserAuthService (dynamic port → assign manually)
start cmd /k "cd UserAuthService && mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8082"

:: ProductCatalogService (dynamic port)
start cmd /k "cd ProductCatalogService && mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8084"

:: OrderService (fixed port)
start cmd /k "cd OrderService && mvn spring-boot:run"

:: PaymentService (assign port)
start cmd /k "cd PaymentService && mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8085"

:: EmailService (fixed port 8081)
start cmd /k "cd EmailService && mvn spring-boot:run"

timeout /t 8

echo ===============================
echo Starting API Gateway
echo ===============================
start cmd /k "cd APIGateway && mvn spring-boot:run"

echo ===============================
echo ALL SERVICES STARTED 🚀
echo ===============================
pause