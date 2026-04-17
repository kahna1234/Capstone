package com.dev.ecommerce.paymentservice.strategies;

import com.dev.ecommerce.paymentservice.services.PaymentGatewayType;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentGatewayChooserStrategy {
    @Autowired
    private Razorpay razorpayGateway;

    @Autowired
    private Stripe stripeGateway;


    public PaymentGateway getBestPaymentGateway(String gateway) throws StripeException {
        /*
        write an algorithm to choose your gateway
         */
        if("STRIPE".equals(gateway)) {
            return stripeGateway;
        } else {
            return razorpayGateway;
        }
        // logic to choose the best payment gateway based on the request
    }
}
