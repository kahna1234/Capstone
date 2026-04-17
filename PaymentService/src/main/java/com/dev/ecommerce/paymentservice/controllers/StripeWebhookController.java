package com.dev.ecommerce.paymentservice.controllers;

import com.dev.ecommerce.paymentservice.services.IStripeWebhookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stripewebhook")
public class StripeWebhookController {

    private static final Logger logger = LoggerFactory.getLogger(StripeWebhookController.class);

    @Autowired
    private IStripeWebhookService stripeWebhookService;

    /**
     * Listen to Stripe webhook events
     * Note: In production, verify the webhook signature for security
     *
     * @param eventPayload The webhook event payload from Stripe
     * @return ResponseEntity with status code
     */
    @PostMapping
    public ResponseEntity<String> listenToStripeWebhook(@RequestBody String eventPayload,
                                                       @RequestHeader("Stripe-Signature") String stripeSignature) {
        try {
            logger.info("Received Stripe webhook event");

            // Delegate to service to handle the webhook event
            stripeWebhookService.handleStripeWebhookEvent(eventPayload, stripeSignature);

            logger.info("Stripe webhook event processed successfully");
            return ResponseEntity.ok("Webhook processed successfully");

        } catch (Exception e) {
            logger.error("Error processing Stripe webhook", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error processing webhook: " + e.getMessage());
        }
    }
}
