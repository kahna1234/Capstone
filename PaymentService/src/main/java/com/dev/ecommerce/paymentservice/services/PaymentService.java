package com.dev.ecommerce.paymentservice.services;

import com.dev.ecommerce.paymentservice.strategies.PaymentGateway;
import com.dev.ecommerce.paymentservice.strategies.PaymentGatewayChooserStrategy;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService implements IPaymentService{
    @Autowired
    private PaymentGatewayChooserStrategy paymentGatewayChooserStrategy;

    @Override
    public String generatePaymentLink(String orderId,
                                      Long amount,
                                      String phoneNumber,
                                      String name,
                                      String email,
                                      String gateway) throws RazorpayException, StripeException {

        PaymentGateway paymentGateway = paymentGatewayChooserStrategy.getBestPaymentGateway(gateway);


        return paymentGateway.generatePaymentLink(
                orderId,
                amount,
                phoneNumber,
                name,
                email);
    }
}
