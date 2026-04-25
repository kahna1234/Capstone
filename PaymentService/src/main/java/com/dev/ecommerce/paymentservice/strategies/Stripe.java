package com.dev.ecommerce.paymentservice.strategies;

import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Stripe implements  PaymentGateway {
    @Value("${stripe.apiKey}")
    private String stripeAPIKey;

    @Value("${frontend.baseUrl}")
    private String frontendBaseUrl;

    @Override
    public String generatePaymentLink(String orderId,
                                      Long amount,
                                      String phoneNumber,
                                      String name,
                                      String email) throws RazorpayException, StripeException {

        try {
            com.stripe.Stripe.apiKey = this.stripeAPIKey;

            com.stripe.param.checkout.SessionCreateParams params = com.stripe.param.checkout.SessionCreateParams.builder()
                    .setMode(com.stripe.param.checkout.SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(getFrontendUrl() + "/#payment-success?orderId=" + orderId)
                    .setCancelUrl(getFrontendUrl() + "/#cart")
                    .addLineItem(
                            com.stripe.param.checkout.SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("inr")
                                                    .setUnitAmount(amount * 100) // Amount in cents
                                                    .setProductData(
                                                            com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Order ID: " + orderId)
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .putMetadata("orderId", orderId)
                    .setPaymentIntentData(
                            com.stripe.param.checkout.SessionCreateParams.PaymentIntentData.builder()
                                    .putMetadata("orderId", orderId)
                                    .build()
                    )
                    .setCustomerEmail(email)
                    .build();

            com.stripe.model.checkout.Session session = com.stripe.model.checkout.Session.create(params);

            // Return the checkout session URL for frontend to redirect to
            System.out.println("✅ Stripe Checkout Session created successfully: " + session.getId());
            return session.getUrl();
        } catch (StripeException e) {
            System.err.println("❌ Stripe API Error: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("❌ Unexpected error in Stripe payment: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create checkout session: " + e.getMessage(), e);
        }
    }

    private String getFrontendUrl() {
        return frontendBaseUrl.endsWith("/")
                ? frontendBaseUrl.substring(0, frontendBaseUrl.length() - 1)
                : frontendBaseUrl;
    }
}
