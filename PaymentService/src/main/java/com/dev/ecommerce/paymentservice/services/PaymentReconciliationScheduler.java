package com.dev.ecommerce.paymentservice.services;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionListParams;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Component
public class PaymentReconciliationScheduler {

    private static final Logger logger = LoggerFactory.getLogger(PaymentReconciliationScheduler.class);

    private final StripeWebhookService stripeWebhookService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${stripe.apiKey}")
    private String stripeApiKey;

    @Value("${payment.reconciliation.enabled:true}")
    private boolean reconciliationEnabled;

    @Value("${payment.reconciliation.lookbackMinutes:60}")
    private long lookbackMinutes;

    @Value("${payment.reconciliation.minAgeMinutes:5}")
    private long minAgeMinutes;

    @Value("${payment.reconciliation.cron:0 */5 * * * *}")
    private String cronExpression;

    @Value("${order.service.url:http://orderservice}")
    private String orderServiceUrl;

    public PaymentReconciliationScheduler(StripeWebhookService stripeWebhookService) {
        this.stripeWebhookService = stripeWebhookService;
    }

    @PostConstruct
    public void init() {
        logger.info("PaymentReconciliationScheduler initialized - Enabled: {}, Cron: '{}', Lookback: {} min, MinAge: {} min",
                reconciliationEnabled, cronExpression, lookbackMinutes, minAgeMinutes);
        if (!reconciliationEnabled) {
            logger.warn("Payment reconciliation is DISABLED. Set payment.reconciliation.enabled=true to enable.");
        }
    }

    @Scheduled(fixedDelay = 300000) // Every 5 minutes (300000 ms)
    public void reconcilePayments() {
        logger.info("Payment reconciliation job triggered");
        
        if (!reconciliationEnabled) {
            logger.debug("Payment reconciliation is disabled, skipping");
            return;
        }

        try {
            Stripe.apiKey = stripeApiKey;

            long now = Instant.now().getEpochSecond();
            long createdAfter = now - (lookbackMinutes * 60);
            long createdBefore = now - (minAgeMinutes * 60);

            logger.debug("Querying Stripe sessions from {} to {} (epoch seconds)", createdAfter, createdBefore);

            SessionListParams params = SessionListParams.builder()
                    .setLimit(100L)
                    .setCreated(
                            SessionListParams.Created.builder()
                                    .setGte(createdAfter)
                                    .setLte(createdBefore)
                                    .build()
                    )
                    .build();

            int confirmedOrders = 0;
            int cancelledOrders = 0;
            int skippedOrders = 0;
            
            var sessions = Session.list(params).getData();
            logger.debug("Found {} Stripe sessions to process", sessions.size());
            
            for (Session session : sessions) {
                String orderId = session.getMetadata() != null ? session.getMetadata().get("orderId") : null;
                if (orderId == null || orderId.isBlank()) {
                    logger.debug("Skipping session with no orderId: {}", session.getId());
                    continue;
                }

                try {
                    // Check current order status before updating
                    String currentStatus = getOrderStatus(orderId);
                    logger.debug("Order {} current status: {}", orderId, currentStatus);

                    // Only process if order is PENDING or status is unknown (auth error)
                    if (!"PENDING".equalsIgnoreCase(currentStatus) && !"UNKNOWN".equalsIgnoreCase(currentStatus)) {
                        logger.debug("Skipping order {} - status is not PENDING (current: {})", orderId, currentStatus);
                        skippedOrders++;
                        continue;
                    }

                    // Check payment status and update accordingly
                    if ("complete".equalsIgnoreCase(session.getStatus())
                            && "paid".equalsIgnoreCase(session.getPaymentStatus())) {
                        logger.info("Payment successful for order {}, updating to CONFIRMED", orderId);
                        stripeWebhookService.handlePaymentIntentSucceeded(orderId);
                        confirmedOrders++;
                        continue;
                    }

                    if (isFailedOrExpiredSession(session)) {
                        logger.info("Payment failed for order {}, updating to CANCELLED", orderId);
                        stripeWebhookService.handlePaymentIntentFailed(orderId);
                        cancelledOrders++;
                    }
                } catch (Exception e) {
                    logger.error("Failed to reconcile payment for order {}", orderId, e);
                }
            }

            if (confirmedOrders > 0 || cancelledOrders > 0 || skippedOrders > 0) {
                logger.info(
                        "Payment reconciliation completed - Confirmed: {}, Cancelled: {}, Skipped (not pending): {}",
                        confirmedOrders,
                        cancelledOrders,
                        skippedOrders
                );
            } else {
                logger.debug("Payment reconciliation completed - no orders needed reconciliation");
            }
        } catch (Exception e) {
            logger.error("Payment reconciliation job failed", e);
        }
    }

    private String getOrderStatus(String orderId) {
        try {
            // Use webhook endpoint that doesn't require authentication
            String url = orderServiceUrl + "/orders/" + orderId + "/status/webhook";
            
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    String.class
            );
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                // Parse the response to extract status
                // Response format: {"status":"PENDING"}
                String body = response.getBody();
                if (body.contains("\"status\"")) {
                    int statusIndex = body.indexOf("\"status\"");
                    int valueStart = body.indexOf("\"", statusIndex + 8) + 1;
                    int valueEnd = body.indexOf("\"", valueStart);
                    return body.substring(valueStart, valueEnd).toUpperCase();
                }
            }
            return "UNKNOWN";
        } catch (Exception e) {
            // If status check fails, return UNKNOWN to proceed with reconciliation
            logger.debug("Failed to get order status for order {}, assuming PENDING", orderId);
            return "UNKNOWN";
        }
    }

    private boolean isFailedOrExpiredSession(Session session) {
        if ("expired".equalsIgnoreCase(session.getStatus())) {
            return true;
        }

        return !"paid".equalsIgnoreCase(session.getPaymentStatus())
                && !"complete".equalsIgnoreCase(session.getStatus());
    }
}
