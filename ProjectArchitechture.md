┌─────────────────────────────────────────────────────────────────────────┐
│                            CLIENT (Frontend)                            │
└────────────────┬───────────────────────────────┬──────────────────────┘
│                               │
┌────────▼─────────┐         ┌──────────▼──────────┐
│ UserAuthService  │         │ ProductCatalog      │
│ (Port: 8080)     │         │ (Port: 8082)        │
│ JWT + Security   │         │ Search & Browse     │
└────────┬─────────┘         └──────────┬──────────┘
│                              │
└──────────┬───────────────────┘
│
┌──────▼─────────┐
│  OrderService  │  ◄── NEW WITH HIBERNATE
│  (Port: 8083)  │
│  Manages Orders│
└──────┬─────────┘
│
┌──────────┼──────────┐
│          │          │
┌────────▼─┐  ┌─────▼────┐   └────────────┐
│ Payment  │  │  Email   │   Eureka Svc  │
│ Service  │  │ Service  │   Discovery   │
│ (8084)   │  │ (8081)   │   (8761)      │
└────┬─────┘  └─────┬────┘   └────────────┘
│              │
┌────────▼──────────────▼────────┐
│                                 │
│     KAFKA MESSAGE BROKER        │
│  (Asynchronous Communication)   │
│  • "Signup" Topic               │
│  • "OrderCreated" Topic         │
│                                 │
└─────────────────────────────────┘

    ┌──────────────────────────────────┐
    │     DATABASE LAYER               │
    │ ┌────────────────────────────┐   │
    │ │ MySQL (Persistent Data)    │   │
    │ │ • User profiles            │   │
    │ │ • Products                 │   │
    │ └────────────────────────────┘   │
    │ ┌────────────────────────────┐   │
    │ │ H2 (OrderService - NEW!)   │   │
    │ │ • Orders & Order Items     │   │
    │ └────────────────────────────┘   │
    └──────────────────────────────────┘


┌────────────────────────────────────────────────────────────────────┐
│                      USER SIGNUP PROCESS                           │
└────────────────────────────────────────────────────────────────────┘

STEP 1: User Registration
┌─────────────────┐
│ Client/Frontend │
│ POST /signup    │
│ {name, email,   │
│  password}      │
└────────┬────────┘
│
▼
┌─────────────────────────────────────────┐
│ UserAuthService - AuthController        │
│ POST /auth/signup                       │
└────────┬────────────────────────────────┘
│
▼
┌────────────────────────────────────────────┐
│ AuthService.signup()                      │
│ • Check if email already exists           │
│ • Hash password using BCrypt              │
│ • Create User entity                      │
│ • Assign DEFAULT role                     │
│ • Set created_at, updated_at timestamps   │
│ • Save to MySQL Database                  │
└────────┬───────────────────────────────────┘
│
▼
┌────────────────────────────────────────────┐
│ Response: HTTP 201                        │
│ {id, name, email, roles}                  │
└────────────────────────────────────────────┘

STEP 2: Send Welcome Email (Asynchronous)
┌────────────────────────────────────────────┐
│ KafkaProducerClient.sendMessage()          │
│ Topic: "Signup"                            │
│ Message: EmailDto {                        │
│   to: user@example.com,                    │
│   from: noreply@app.com,                   │
│   subject: "Welcome!",                     │
│   body: "Thanks for signing up..."         │
│ }                                          │
└────────┬───────────────────────────────────┘
│
▼
┌──────────────────────────────────────────────┐
│ KAFKA BROKER - "Signup" Topic               │
│ (Message Queue - Persistent)                │
└────────┬─────────────────────────────────────┘
│
▼
┌──────────────────────────────────────────────┐
│ EmailService - KafkaConsumerClient          │
│ @KafkaListener(topics="Signup")             │
│ • Deserialize EmailDto                      │
│ • Configure SMTP (Gmail)                    │
│ • Send email via JavaMail                   │
│ • Log success/failure                       │
└──────────────────────────────────────────────┘


┌────────────────────────────────────────────────────────────────────┐
│                      USER LOGIN PROCESS                            │
└────────────────────────────────────────────────────────────────────┘

STEP 1: Login Request
┌─────────────────┐
│ Client/Frontend │
│ POST /login     │
│ {email,         │
│  password}      │
└────────┬────────┘
│
▼
┌─────────────────────────────────────────┐
│ UserAuthService - AuthController        │
│ POST /auth/login                        │
└────────┬────────────────────────────────┘
│
▼
┌──────────────────────────────────────────┐
│ AuthService.login()                     │
│ • Find user by email in MySQL DB        │
│ • Compare entered password with         │
│   stored BCrypt hash                    │
│ • If match: Generate JWT Token          │
│   JWT = HMAC-SHA256(header.payload)     │
│ • Create Session record                 │
│ • Save session to MySQL                 │
└────────┬─────────────────────────────────┘
│
▼
┌──────────────────────────────────────────┐
│ Response: HTTP 200                      │
│ Body: {id, name, email, roles}          │
│ Header: Cookie = JWT_TOKEN              │
│ (HttpOnly, Secure flags set)            │
└──────────────────────────────────────────┘

STEP 2: Future Requests with JWT
┌────────────────────────────────────┐
│ All subsequent API calls include   │
│ Cookie: JWT_TOKEN                  │
│                                    │
│ OR                                 │
│                                    │
│ Authorization: Bearer JWT_TOKEN    │
└────────────────────────────────────┘

STEP 3: Token Validation
┌─────────────────────────────────────────┐
│ Protected Endpoints (e.g., /orders)     │
│ ↓                                       │
│ Extract JWT from request                │
│ ↓                                       │
│ AuthService.validateToken()             │
│ • Verify signature using SecretKey      │
│ • Check expiration time                 │
│ • Decode claims                        │
│ ↓                                       │
│ If valid → Allow request                │
│ If invalid → Return HTTP 401/403        │
└─────────────────────────────────────────┘


┌────────────────────────────────────────────────────────────────────┐
│                    ORDER CREATION PROCESS                          │
│                  (NEW with Hibernate & H2 DB)                      │
└────────────────────────────────────────────────────────────────────┘

STEP 1: Create Order Request
┌─────────────────────┐
│ Client/Frontend     │
│ POST /orders        │
│ Auth: JWT Token     │
│ {                   │
│   userId: 1,        │
│   items: [          │
│     {               │
│       productId: 5, │
│       quantity: 2,  │
│       price: 29.99  │
│     }               │
│   ],                │
│   status: "PENDING",│
│   totalAmount: 59.98│
│ }                   │
└────────┬────────────┘
│
▼
┌──────────────────────────────────────┐
│ OrderService - OrderController       │
│ POST /orders                         │
│ • Verify JWT token                   │
│ • Extract user_id from JWT           │
└────────┬───────────────────────────────┘
│
▼
┌──────────────────────────────────────────┐
│ OrderService.createOrder(OrderDto)      │
│                                          │
│ 1. Validate order data                  │
│    • Check items not empty              │
│    • Verify total amount matches        │
│                                          │
│ 2. Convert DTO → JPA Entities          │
│    OrderDto → Order (JPA Entity)        │
│    OrderItemDto → OrderItem (JPA)       │
│                                          │
│ 3. Persist to Database                  │
│    orderRepository.save(order)          │
│    ↓                                    │
│    Hibernate intercepts save()          │
│    ↓                                    │
│    Generates SQL (if table missing):    │
│    CREATE TABLE orders (                │
│      id BIGINT PRIMARY KEY,             │
│      user_id BIGINT,                    │
│      status VARCHAR(255),               │
│      total_amount DOUBLE                │
│    )                                    │
│    CREATE TABLE order_item (            │
│      id BIGINT PRIMARY KEY,             │
│      product_id BIGINT,                 │
│      quantity INT,                      │
│      price DOUBLE,                      │
│      order_id BIGINT (FOREIGN KEY)      │
│    )                                    │
│    ↓                                    │
│    4. Execute INSERT statements        │
│    INSERT INTO orders VALUES(...)      │
│    INSERT INTO order_item VALUES(...)  │
│                                          │
│    5. Commit transaction                │
└────────┬───────────────────────────────┘
│
▼
┌──────────────────────────────────────────┐
│ Publish Kafka Event (Asynchronous)      │
│ KafkaProducerClient.sendOrderConfirmation│
│ Topic: "OrderCreated"                    │
│ Message: OrderDto (same object)          │
└────────┬───────────────────────────────┘
│
▼
┌─────────────────────────────────────┐
│ KAFKA BROKER - "OrderCreated" Topic │
└────────┬──────────────────────────────┘
│
▼ (Optional - if EmailService listening)
┌──────────────────────────────────────┐
│ EmailService receives order event    │
│ (KafkaConsumerClient - if configured)│
│ • Send order confirmation email      │
└──────────────────────────────────────┘

STEP 2: Return Response
┌─────────────────────────────────────┐
│ Response: HTTP 201/200              │
│ {                                   │
│   id: 1,        (DB generated)      │
│   userId: 1,                        │
│   items: [...], (with saved IDs)    │
│   status: "PENDING",                │
│   totalAmount: 59.98                │
│ }                                   │
└─────────────────────────────────────┘

DATABASE STATE AFTER ORDER CREATION:
┌───────────────────────────────────────────────┐
│ H2 Database - OrderService                    │
├─────────────────┬──────────────────────────────┤
│   orders table  │  order_item table            │
├─────┬───┬──────┼──────┬──────┬────┬──────┬─────┤
│id   │uid│status│id    │prod  │qty │price │oid  │
├─────┼───┼──────┼──────┼──────┼────┼──────┼─────┤
│ 1   │ 1 │PENDING│ 1    │ 5    │ 2  │29.99│ 1   │
└─────┴───┴──────┴──────┴──────┴────┴──────┴─────┘

Relationship: order_id in order_item = orders.id (1:N)



┌────────────────────────────────────────────────────────────────────┐
│                    GET ORDER DETAILS PROCESS                       │
└────────────────────────────────────────────────────────────────────┘

STEP 1: Request Order Details
┌─────────────────────┐
│ Client/Frontend     │
│ GET /orders/1       │
│ Auth: JWT Token     │
└────────┬────────────┘
│
▼
┌──────────────────────────────────────┐
│ OrderService - OrderController       │
│ GET /orders/{id}                     │
│ • Verify JWT token                   │
└────────┬───────────────────────────────┘
│
▼
┌────────────────────────────────────────┐
│ OrderService.getOrder(orderId)        │
│ • Call orderRepository.findById(id)   │
│                                       │
│   Hibernate generates:                │
│   SELECT * FROM orders WHERE id = ?   │
│   SELECT * FROM order_item            │
│     WHERE order_id = ?                │
│                                       │
│ • Map Order entity to OrderDto        │
│ • Map OrderItem entities to DTOs      │
└────────┬────────────────────────────────┘
│
▼
┌────────────────────────────────────────┐
│ Response: HTTP 200                    │
│ {                                     │
│   id: 1,                              │
│   userId: 1,                          │
│   items: [                            │
│     {productId: 5, quantity: 2,...}   │
│   ],                                  │
│   status: "PENDING",                  │
│   totalAmount: 59.98                  │
│ }                                     │
└────────────────────────────────────────┘


┌────────────────────────────────────────────────────────────────────┐
│                 PAYMENT PROCESSING PROCESS                         │
└────────────────────────────────────────────────────────────────────┘

STEP 1: Initiate Payment
┌─────────────────────┐
│ Client/Frontend     │
│ POST /api/payments  │
│ {                   │
│   orderId: 1,       │
│   amount: 5998,     │
│   phoneNumber: ..., │
│   name: ...,        │
│   email: ...,       │
│   paymentGateway:   │
│   "STRIPE" or       │
│   "RAZORPAY"        │
│ }                   │
└────────┬────────────┘
│
▼
┌────────────────────────────────────────────┐
│ PaymentService - PaymentController         │
│ POST /api/payments                         │
│ • Validate input data                      │
│ • Extract gateway type                     │
└────────┬───────────────────────────────────┘
│
▼
┌────────────────────────────────────────────┐
│ PaymentService.generatePaymentLink()       │
│ • Call PaymentGatewayChooserStrategy       │
│   to select best gateway                   │
└────────┬───────────────────────────────────┘
│
├─────────────────────────────────┐
│                                 │
▼                                 ▼
┌────────────────────┐         ┌───────────────────┐
│ STRIPE GATEWAY     │         │ RAZORPAY GATEWAY  │
│                    │         │                   │
│ • Call Stripe API  │         │ • Call Razorpay   │
│ • Create Checkout  │         │   API             │
│ • Return Link URL  │         │ • Create Payment  │
│   e.g.,            │         │   Order           │
│   checkout.stripe  │         │ • Return Link URL │
│   .com/pay/...     │         └───────────────────┘
└────────┬───────────┘
│
└─────────────────────────────────┐
│
┌─────────────────────────────────┘
│
▼
┌────────────────────────────────────────────┐
│ Response: HTTP 200                        │
│ {                                          │
│   "https://checkout.stripe.com/pay/..."   │
│   OR                                       │
│   "https://razorpay.com/..."               │
│ }                                          │
└────────────────────────────────────────────┘

STEP 2: User Completes Payment
┌──────────────────────────────────────────┐
│ Frontend redirects to payment link       │
│ User enters card details                 │
│ Payment gateway processes payment        │
│ Payment confirmed/declined               │
└──────────────────────────────────────────┘

STEP 3: Webhook Callback (Optional)
┌──────────────────────────────────────────┐
│ Stripe/Razorpay calls webhook endpoint   │
│ PaymentService receives confirmation     │
│ Update order status in OrderService      │
│ Publish event to Kafka                   │
└──────────────────────────────────────────┘


┌────────────────────────────────────────────────────────────────────┐
│              SERVICE REGISTRATION & DISCOVERY                      │
└────────────────────────────────────────────────────────────────────┘

STEP 1: Service Startup & Registration
┌─────────────────────────────────────┐
│ OrderService starts                 │
│ (OrderServiceApplication.main())    │
└────────┬────────────────────────────┘
│
▼
┌─────────────────────────────────────────────┐
│ Spring Boot Auto-Configuration             │
│ Detects: @EnableEurekaClient annotation     │
│ (or configured in pom.xml)                  │
└────────┬────────────────────────────────────┘
│
▼
┌─────────────────────────────────────────────┐
│ Eureka Client Initializes                   │
│ Reads configuration:                        │
│ • spring.application.name=OrderService      │
│ • eureka.instance.hostname=localhost        │
│ • server.port=8083                          │
│ • eureka.client.service-url.defaultZone=    │
│   http://localhost:8761/eureka/             │
└────────┬────────────────────────────────────┘
│
▼
┌──────────────────────────────────────────────┐
│ POST to Eureka Server                       │
│ Register OrderService Instance               │
│ {                                            │
│   serviceName: "orderservice",               │
│   hostname: "localhost",                     │
│   port: 8083,                                │
│   ipAddress: "127.0.0.1",                    │
│   status: "UP",                              │
│   healthCheckUrl: "http://localhost:8083/   │
│                    health"                   │
│ }                                            │
└────────┬───────────────────────────────────┘
│
▼
┌──────────────────────────────────────────────┐
│ Eureka Server (Port 8761)                   │
│ ┌──────────────────────────────────────────┐│
│ │ Service Registry (In-Memory)             ││
│ │                                          ││
│ │ orderservice: [                          ││
│ │   {                                      ││
│ │     hostname: localhost,                 ││
│ │     port: 8083,                          ││
│ │     status: UP,                          ││
│ │     lastHeartbeat: 2026-03-29T20:48:00  ││
│ │   }                                      ││
│ │ ]                                        ││
│ │                                          ││
│ │ userauth: [                              ││
│ │   {hostname: localhost, port: 8080,...} ││
│ │ ]                                        ││
│ │                                          ││
│ │ productcatalog: [                        ││
│ │   {hostname: localhost, port: 8082,...} ││
│ │ ]                                        ││
│ │                                          ││
│ │ paymentservice: [                        ││
│ │   {hostname: localhost, port: 8084,...} ││
│ │ ]                                        ││
│ │                                          ││
│ │ emailservice: [                          ││
│ │   {hostname: localhost, port: 8081,...} ││
│ │ ]                                        ││
│ └──────────────────────────────────────────┘│
└──────────────────────────────────────────────┘

STEP 2: Periodic Heartbeats
┌──────────────────────────────────────────┐
│ Every 30 seconds, each service sends     │
│ heartbeat to Eureka:                     │
│ PUT /eureka/apps/ORDERSERVICE/instance  │
│                                          │
│ If Eureka doesn't receive 3 heartbeats  │
│ (90 seconds), service marked as DOWN     │
└──────────────────────────────────────────┘

STEP 3: Service Discovery (Inter-service calls)
┌────────────────────────────────────────┐
│ Scenario: OrderService needs to       │
│ verify product in ProductCatalog      │
└────────┬───────────────────────────────┘
│
▼
┌────────────────────────────────────────┐
│ OrderService Eureka Client             │
│ discoveryClient.getInstances(          │
│   "productcatalog"                     │
│ )                                      │
└────────┬───────────────────────────────┘
│
▼
┌────────────────────────────────────────┐
│ Eureka Server returns:                 │
│ [                                      │
│   {                                    │
│     url: http://localhost:8082,        │
│     status: UP,                        │
│     ipAddress: 127.0.0.1               │
│   }                                    │
│ ]                                      │
└────────┬───────────────────────────────┘
│
▼
┌────────────────────────────────────────┐
│ OrderService makes HTTP call:          │
│ GET http://localhost:8082/products/5   │
│                                        │
│ Response: Product details              │
└────────────────────────────────────────┘


┌────────────────────────────────────────────────────────────────────┐
│            KAFKA MESSAGE QUEUE ARCHITECTURE                        │
└────────────────────────────────────────────────────────────────────┘

TOPIC 1: "Signup"
Producer: UserAuthService (KafkaProducerClient)
Consumer: EmailService (KafkaConsumerClient)

    ┌────────────────────────────────────────┐
    │ UserAuthService.signup() completes     │
    │ ↓                                      │
    │ kafkaProducerClient.sendMessage(       │
    │   "Signup",                            │
    │   EmailDto.toJson()                    │
    │ )                                      │
    │ ↓                                      │
    │ KafkaTemplate sends message to broker │
    └────────┬───────────────────────────────┘
             │
             ▼
    ┌──────────────────────────────────────────┐
    │ KAFKA BROKER                             │
    │ Topic: "Signup"                          │
    │ Partition 0: [msg1, msg2, msg3, ...]    │
    │ Offset: 0, 1, 2, 3, ...                  │
    │ Retention: 7 days (default)              │
    └────────┬─────────────────────────────────┘
             │
             ▼ (Pull-based, triggered by @KafkaListener)
    ┌────────────────────────────────────────────┐
    │ EmailService - KafkaConsumerClient         │
    │ @KafkaListener(                            │
    │   topics="Signup",                         │
    │   groupId="emailService"                   │
    │ )                                          │
    │ public void sendEmail(String message) {   │
    │   EmailDto emailDto = deserialize(message) │
    │   sendViaGmail(emailDto)                   │
    │ }                                          │
    └────────────────────────────────────────────┘

TOPIC 2: "OrderCreated"
Producer: OrderService (KafkaProducerClient)
Consumer: EmailService (optional, if configured)

    ┌────────────────────────────────────────┐
    │ OrderService.createOrder() completes   │
    │ ↓                                      │
    │ kafkaProducerClient.sendOrderConfirm() │
    │ Topic: "OrderCreated"                  │
    │ Message: OrderDto.toJson()             │
    └────────┬───────────────────────────────┘
             │
             ▼
    ┌──────────────────────────────────────────┐
    │ KAFKA BROKER                             │
    │ Topic: "OrderCreated"                    │
    │ Partition 0: [order1, order2, ...]      │
    └────────┬─────────────────────────────────┘
             │
             ▼ (Optional - EmailService can listen)
    ┌────────────────────────────────────────────┐
    │ EmailService receives order event (if      │
    │ KafkaListener configured for OrderCreated) │
    │ • Send order confirmation email to user    │
    └────────────────────────────────────────────┘


USER AUTH SERVICE (MySQL)
┌─────────────────────┐
│      users table    │
├─────────────────────┤
│ id (PK)             │
│ email (UNIQUE)      │
│ name                │
│ password (hashed)   │
│ created_at          │
│ updated_at          │
│ state (ACTIVE)      │
└──────────┬──────────┘
│
│ M:M (Many to Many)
│
▼
┌──────────────────────────┐       ┌────────────────┐
│ user_roles (Join table)  │       │ roles table    │
├──────────────────────────┤       ├────────────────┤
│ user_id (FK)             │───┐   │ id (PK)        │
│ role_id (FK)             │   └──▶│ name (DEFAULT) │
└──────────────────────────┘       └────────────────┘

┌─────────────────────────┐
│   sessions table        │
├─────────────────────────┤
│ id (PK)                 │
│ user_id (FK)           │
│ token (JWT)            │
│ created_at             │
│ expires_at             │
└─────────────────────────┘


PRODUCT CATALOG SERVICE (MySQL)
┌──────────────────────┐
│ products table       │
├──────────────────────┤
│ id (PK)              │
│ title                │
│ description          │
│ price                │
│ stock_quantity       │
│ created_at           │
│ updated_at           │
└──────────────────────┘


ORDER SERVICE (H2 - In-Memory) - NEW!
┌──────────────────────┐       ┌──────────────────────┐
│   orders table       │       │  order_item table    │
├──────────────────────┤       ├──────────────────────┤
│ id (PK)              │◄──────│ id (PK)              │
│ user_id              │ 1:N   │ order_id (FK)        │
│ status (PENDING,     │       │ product_id           │
│  CONFIRMED, SHIPPED) │       │ quantity             │
│ total_amount         │       │ price                │
└──────────────────────┘       └──────────────────────┘


PAYMENT SERVICE (No DB - API Integration Only)
Uses Stripe & Razorpay APIs:
• Stripe: https://api.stripe.com/
• Razorpay: https://api.razorpay.com/


JWT Token Structure:
┌──────────────┬──────────────┬──────────────┐
│   HEADER     │   PAYLOAD    │  SIGNATURE   │
├──────────────┼──────────────┼──────────────┤
│ {            │ {            │ HMAC-SHA256( │
│   typ: JWT   │   sub: 1,    │   base64(    │
│   alg: HS256 │   email: ... │   header) +  │
│ }            │   roles: [], │   "." +      │
│              │   exp: ...   │   base64(    │
│              │ }            │   payload),  │
│              │              │   secret)    │
└──────────────┴──────────────┴──────────────┘

Base64Url(header).Base64Url(payload).signature

Example: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...


1. USER VISITS APP
   ↓
2. SIGNUP
   → POST /auth/signup {name, email, password}
   → UserAuthService validates & saves user
   → Publishes "Signup" event to Kafka
   → EmailService sends welcome email
   ↓
3. LOGIN
   → POST /auth/login {email, password}
   → UserAuthService returns JWT in cookie
   ↓
4. BROWSE PRODUCTS
   → GET /products (with JWT)
   → ProductCatalogService returns products
   ↓
5. CREATE ORDER
   → POST /orders {userId, items[], status, totalAmount}
   → OrderService saves to H2 DB with Hibernate
   → Creates orders & order_item tables automatically
   → Publishes "OrderCreated" event to Kafka
   ↓
6. CHECKOUT & PAYMENT
   → POST /api/payments {orderId, amount, ...}
   → PaymentService generates payment link (Stripe/Razorpay)
   → Frontend redirects to payment gateway
   ↓
7. PROCESS PAYMENT
   → User enters payment details on gateway
   → Payment confirmed/declined
   ↓
8. VIEW ORDER
   → GET /orders/{id} (with JWT)
   → OrderService retrieves from H2 DB
   → Returns order + items details
   ↓
9. EMAIL CONFIRMATIONS
   → Signup confirmation email
   → Order confirmation email (optional)
