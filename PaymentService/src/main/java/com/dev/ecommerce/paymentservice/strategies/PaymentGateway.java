package com.dev.ecommerce.paymentservice.strategies;

import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

public interface PaymentGateway {
    String generatePaymentLink(String orderId,
                               Long amount,
                               String phoneNumber,
                               String name,
                               String email) throws RazorpayException, StripeException;
}
