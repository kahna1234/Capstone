package com.dev.ecommerce.paymentservice.services;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionListParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class PaymentReconciliationScheduler {

    private static final Logger logger = LoggerFactory.getLogger(PaymentReconciliationScheduler.class);

    private final StripeWebhookService stripeWebhookService;

    @Value("${stripe.apiKey}")
    private String stripeApiKey;

    @Value("${payment.reconciliation.enabled:true}")
    private boolean reconciliationEnabled;

    @Value("${payment.reconciliation.lookbackMinutes:60}")
    private long lookbackMinutes;

    @Value("${payment.reconciliation.minAgeMinutes:5}")
    private long minAgeMinutes;

    public PaymentReconciliationScheduler(StripeWebhookService stripeWebhookService) {
        this.stripeWebhookService = stripeWebhookService;
    }

    @Scheduled(cron = "${payment.reconciliation.cron:0 */5 * * * *}")
    public void reconcilePayments() {
        if (!reconciliationEnabled) {
            return;
        }

        try {
            Stripe.apiKey = stripeApiKey;

            long now = Instant.now().getEpochSecond();
            long createdAfter = now - (lookbackMinutes * 60);
            long createdBefore = now - (minAgeMinutes * 60);

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
            int failedOrders = 0;
            for (Session session : Session.list(params).getData()) {
                String orderId = session.getMetadata() != null ? session.getMetadata().get("orderId") : null;
                if (orderId == null || orderId.isBlank()) {
                    continue;
                }

                try {
                    if ("complete".equalsIgnoreCase(session.getStatus())
                            && "paid".equalsIgnoreCase(session.getPaymentStatus())) {
                        stripeWebhookService.handlePaymentIntentSucceeded(orderId);
                        confirmedOrders++;
                        continue;
                    }

                    if (isFailedOrExpiredSession(session)) {
                        stripeWebhookService.handlePaymentIntentFailed(orderId);
                        failedOrders++;
                    }
                } catch (Exception e) {
                    logger.warn("Failed to reconcile payment for order {}", orderId, e);
                }
            }

            if (confirmedOrders > 0 || failedOrders > 0) {
                logger.info(
                        "Payment reconciliation repaired {} confirmed orders and {} failed orders",
                        confirmedOrders,
                        failedOrders
                );
            }
        } catch (Exception e) {
            logger.error("Payment reconciliation job failed", e);
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
