# API Gateway

Spring Cloud Gateway for all microservices in the Project.

## Features
- Service discovery via Eureka
- Routing to all microservices
- CORS enabled
- Security filter placeholder (JWT-ready)
- Global error handler

## Routes
| Path Prefix         | Service Name         | Target URI (Eureka)         |
|--------------------|---------------------|-----------------------------|
| `/auth/**`         | userauthservice      | lb://userauthservice        |
| `/users/**`        | userauthservice      | lb://userauthservice        |
| `/products/**`     | productcatalogservice| lb://productcatalogservice  |
| `/search/**`       | productcatalogservice| lb://productcatalogservice  |
| `/api/orders/**`   | OrderService         | lb://OrderService           |
| `/api/payments/**` | PaymentService       | lb://PaymentService         |
| `/stripewebhook/**`| PaymentService       | lb://PaymentService         |
| `/email/**`        | EmailService         | lb://EmailService           |

## How to Run
1. Start Eureka server
2. Start all microservices
3. Start API Gateway (`mvn spring-boot:run`)
4. Access all APIs via `http://localhost:8088/`

## Security
- All routes are open by default. To enable JWT validation, add a custom filter in `SecurityConfig`.

