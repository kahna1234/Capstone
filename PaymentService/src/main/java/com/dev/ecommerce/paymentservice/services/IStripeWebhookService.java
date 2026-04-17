package com.dev.ecommerce.paymentservice.services;

public interface IStripeWebhookService {
    /**
     * Handle Stripe webhook events
     * @param eventPayload The webhook event payload in JSON format
     * @param stripeSignature The Stripe-Signature header for verification
     */
    void handleStripeWebhookEvent(String eventPayload, String stripeSignature);

    /**
     * Handle payment intent succeeded event
     * @param orderId The order ID associated with the payment
     */
    void handlePaymentIntentSucceeded(String orderId);

    /**
     * Handle payment intent failed event
     * @param orderId The order ID associated with the payment
     */
    void handlePaymentIntentFailed(String orderId);
}
