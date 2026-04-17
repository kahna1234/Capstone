# Stripe Webhook Service - Usage Examples

## 1. Testing with cURL

### Test Payment Intent Succeeded
```bash
curl -X POST http://localhost:8085/api/stripewebhook \
  -H "Content-Type: application/json" \
  -d '{
    "id": "evt_1NJ5qDARhOQ1l0CIZ7nJT8vD",
    "type": "payment_intent.succeeded",
    "data": {
      "object": {
        "id": "pi_1NJ5qDARhOQ1l0CIZ7nJT8vD",
        "status": "succeeded",
        "metadata": {
          "orderId": "ORDER123456"
        }
      }
    }
  }'
```

**Expected Response:**
```
200 OK
"Webhook processed successfully"
```

### Test Payment Intent Failed
```bash
curl -X POST http://localhost:8085/api/stripewebhook \
  -H "Content-Type: application/json" \
  -d '{
    "id": "evt_1NJ5qDARhOQ1l0CIZ7nJT8vE",
    "type": "payment_intent.payment_failed",
    "data": {
      "object": {
        "id": "pi_1NJ5qDARhOQ1l0CIZ7nJT8vE",
        "metadata": {
          "orderId": "ORDER123457"
        }
      }
    }
  }'
```

## 2. Testing with Stripe CLI

### Step 1: Install Stripe CLI
```bash
# Windows
choco install stripe-cli

# macOS
brew install stripe/stripe-cli/stripe

# Linux
curl -fsSL https://files.stripe.com/stripe-cli/v1.13.0/stripe_linux_x86_64.tar.gz | tar -zxf -
sudo mv stripe /usr/local/bin
```

### Step 2: Authenticate
```bash
stripe login
```

### Step 3: Forward Webhook Events
```bash
stripe listen --forward-to localhost:8085/api/stripewebhook
```

### Step 4: Trigger Test Events (in another terminal)
```bash
# Trigger payment_intent.succeeded
stripe trigger payment_intent.succeeded --add metadata.orderId=ORDER123456

# Trigger payment_intent.payment_failed
stripe trigger payment_intent.payment_failed --add metadata.orderId=ORDER123457
```

## 3. Integration with Payment Link Generation

When generating a payment link, include the orderId in metadata:

```java
// In PaymentService or PaymentController
PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
    .setAmount(amount * 100) // Amount in cents
    .setCurrency("usd")
    .putMetadata("orderId", orderId)
    .setDescription("Order " + orderId)
    .build();

PaymentIntent intent = PaymentIntent.create(params);
```

## 4. Monitoring Webhook Events

### View Application Logs
```bash
# For Spring Boot running locally
# Check console output for log messages like:
# "Processing Stripe webhook event: payment_intent.succeeded"
# "Order ORDER123456 status updated to CONFIRMED"
```

### Enable Debug Logging
Add to `application.properties`:
```properties
logging.level.com.dev.ecommerce.paymentservice.services.StripeWebhookService=DEBUG
logging.level.com.dev.ecommerce.paymentservice.controllers.StripeWebhookController=DEBUG
```

## 5. Programmatic Usage

### Direct Service Usage
```java
@Autowired
private IStripeWebhookService stripeWebhookService;

public void processPaymentManually(String orderId) {
    // Manual payment success handling
    stripeWebhookService.handlePaymentIntentSucceeded(orderId);
    
    // Manual payment failure handling
    stripeWebhookService.handlePaymentIntentFailed(orderId);
}
```

### Using Example Component
```java
@Autowired
private StripeWebhookServiceExample example;

public void testWebhook() {
    // Process a simulated webhook
    example.exampleProcessSuccessfulPayment();
}
```

## 6. Error Handling Examples

### Missing Order ID
If the webhook payload doesn't contain an orderId:
```
Log: "Payment Intent succeeded but no orderId found in metadata. PaymentIntent ID: pi_..."
Response: 200 OK (webhook processed, but order not updated)
```

### OrderService Not Available
If OrderService is unreachable:
```
Response: 400 BAD_REQUEST
Body: "Error processing webhook: Connection refused"
```

### Invalid JSON Payload
If the JSON is malformed:
```
Response: 400 BAD_REQUEST
Body: "Error processing webhook: JSON parsing error"
```

## 7. Configuration Examples

### Application Properties
```properties
# Server
spring.application.name=paymentservice
server.port=8085

# Payment Gateways
razorpay.keyId=rzp_test_xxxx
razorpay.keySecret=xxxx
stripe.apiKey=sk_test_xxxx

# Service URLs
order-service.url=http://localhost:8081
```

### Webhook URL Configuration in Stripe Dashboard

1. Go to https://dashboard.stripe.com/webhooks
2. Click "Add endpoint"
3. Enter your webhook URL: `https://your-domain.com/api/stripewebhook`
4. Select events:
   - `payment_intent.succeeded`
   - `payment_intent.payment_failed`
5. Click "Create endpoint"
6. Copy the webhook signing secret for signature verification

## 8. OrderService Integration Example

### Expected OrderService API
```
PUT /orders/{orderId}/status?status={status}

Example:
PUT http://orderservice/orders/ORDER123456/status?status=CONFIRMED
PUT http://orderservice/orders/ORDER123456/status?status=FAILED
```

### OrderService Response
```json
{
  "orderId": "ORDER123456",
  "status": "CONFIRMED",
  "updatedAt": "2026-04-14T22:46:00Z"
}
```

## 9. Testing Workflow

### Complete End-to-End Flow
```
1. Customer initiates payment
   ↓
2. PaymentService generates payment link
   ↓
3. Customer completes payment in Stripe
   ↓
4. Stripe sends webhook event
   ↓
5. /api/stripewebhook receives event
   ↓
6. StripeWebhookService processes event
   ↓
7. OrderService API called to update status
   ↓
8. Order status changed to CONFIRMED/FAILED
```

## 10. Debugging Tips

### Enable Verbose Logging
```bash
# In application.properties
logging.level.org.springframework.web=DEBUG
logging.level.com.dev.ecommerce.paymentservice=DEBUG
```

### Print Webhook Payload
Add to StripeWebhookService:
```java
logger.debug("Webhook payload: {}", eventPayload);
```

### Check Port Usage
```bash
netstat -ano | findstr ":8085"
```

### Test Service Connectivity
```bash
curl -X PUT http://orderservice/orders/TEST123/status?status=CONFIRMED \
  -H "Content-Type: application/json"
```

---

For more information, see:
- `STRIPE_WEBHOOK_SERVICE.md` - Comprehensive documentation
- `IMPLEMENTATION_SUMMARY.md` - Implementation overview

