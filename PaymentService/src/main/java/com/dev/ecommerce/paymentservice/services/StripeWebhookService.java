package com.dev.ecommerce.paymentservice.services;

import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StripeWebhookService implements IStripeWebhookService {

    private static final Logger logger = LoggerFactory.getLogger(StripeWebhookService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @Override
    public void handleStripeWebhookEvent(String eventPayload, String stripeSignature) {
        try {
            // Verify and extract the event from the request payload
            Event event = Webhook.constructEvent(eventPayload, stripeSignature, webhookSecret);

            logger.info("Processing Stripe webhook event: {}", event.getType());

            // Route to appropriate handler based on event type
            switch (event.getType()) {
                case "checkout.session.completed":
                    handleCheckoutSessionCompletedEvent(event);
                    break;
                case "payment_intent.succeeded":
                    handlePaymentIntentSucceededEvent(event);
                    break;
                case "payment_intent.payment_failed":
                    handlePaymentIntentFailedEvent(event);
                    break;
                default:
                    logger.warn("Unhandled Stripe event type: {}", event.getType());
            }
        } catch (Exception e) {
            logger.error("Error processing Stripe webhook event", e);
            throw new RuntimeException("Failed to process webhook event", e);
        }
    }

    @Override
    public void handlePaymentIntentSucceeded(String orderId) {
        try {
            logger.info("Handling payment succeeded for order: {}", orderId);

            // Call OrderService to update status to CONFIRMED
            String orderServiceUrl = "http://orderservice/orders/" + orderId + "/status?status=CONFIRMED";
            restTemplate.put(orderServiceUrl, null);

            logger.info("Order {} status updated to CONFIRMED", orderId);
        } catch (Exception e) {
            logger.error("Error updating order status for order: {}", orderId, e);
            throw new RuntimeException("Failed to update order status", e);
        }
    }

    @Override
    public void handlePaymentIntentFailed(String orderId) {
        try {
            logger.info("Handling payment failed for order: {}", orderId);

            // Call OrderService to update status to FAILED
            String orderServiceUrl = "http://orderservice/orders/" + orderId + "/status?status=FAILED";
            restTemplate.put(orderServiceUrl, null);

            logger.info("Order {} status updated to FAILED", orderId);
        } catch (Exception e) {
            logger.error("Error updating order status for order: {}", orderId, e);
            throw new RuntimeException("Failed to update order status", e);
        }
    }

    /**
     * Private helper method to handle payment_intent.succeeded event
     */
    private void handlePaymentIntentSucceededEvent(Event event) {
        try {
            PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().get();
            String orderId = paymentIntent.getMetadata().get("orderId");

            if (orderId != null) {
                handlePaymentIntentSucceeded(orderId);
            } else {
                logger.warn("Payment Intent succeeded but no orderId found in metadata. PaymentIntent ID: {}",
                    paymentIntent.getId());
            }
        } catch (Exception e) {
            logger.error("Error handling payment_intent.succeeded event", e);
            throw new RuntimeException("Failed to handle payment succeeded event", e);
        }
    }

    /**
     * Private helper method to handle checkout.session.completed event
     */
    private void handleCheckoutSessionCompletedEvent(Event event) {
        try {
            com.stripe.model.checkout.Session session = (com.stripe.model.checkout.Session) event.getDataObjectDeserializer().getObject().get();
            String orderId = session.getMetadata().get("orderId");

            if (orderId != null) {
                handlePaymentIntentSucceeded(orderId);
            } else {
                logger.warn("Checkout Session completed but no orderId found in metadata. Session ID: {}",
                    session.getId());
            }
        } catch (Exception e) {
            logger.error("Error handling checkout.session.completed event", e);
            throw new RuntimeException("Failed to handle checkout session completed event", e);
        }
    }

    /**
     * Private helper method to handle payment_intent.payment_failed event
     */
    private void handlePaymentIntentFailedEvent(Event event) {
        try {
            PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().get();
            String orderId = paymentIntent.getMetadata().get("orderId");

            if (orderId != null) {
                handlePaymentIntentFailed(orderId);
            } else {
                logger.warn("Payment Intent failed but no orderId found in metadata. PaymentIntent ID: {}",
                    paymentIntent.getId());
            }
        } catch (Exception e) {
            logger.error("Error handling payment_intent.payment_failed event", e);
            throw new RuntimeException("Failed to handle payment failed event", e);
        }
    }
}
