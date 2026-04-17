package com.dev.ecommerce.paymentservice.configurations;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class RazorpayClient {
    @Value("${razorpay.keyId}")
    private String razorpayKeyId;

    @Value("${razorpay.keySecret}")
    private String razorpayKeySecret;

    @Bean
    public com.razorpay.RazorpayClient getRazorpayClient() throws RazorpayException {
        // Initialize Razorpay client with API key and secret
        return new com.razorpay.RazorpayClient(razorpayKeyId, razorpayKeySecret);
    }
}
