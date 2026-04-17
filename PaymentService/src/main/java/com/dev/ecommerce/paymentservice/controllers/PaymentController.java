package com.dev.ecommerce.paymentservice.controllers;

import com.dev.ecommerce.paymentservice.dtos.PaymentRequestDto;
import com.dev.ecommerce.paymentservice.services.IPaymentService;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private IPaymentService paymentService;

    @PostMapping
    public String generatePaymentLink(@RequestBody PaymentRequestDto paymentRequestDto) throws RazorpayException, StripeException {
        try {
            System.out.println("📦 Payment Request: OrderId=" + paymentRequestDto.getOrderId() +
                             ", Amount=" + paymentRequestDto.getAmount() +
                             ", Gateway=" + paymentRequestDto.getPaymentGatewayType());

            String result = paymentService.generatePaymentLink(paymentRequestDto.getOrderId(),
                    paymentRequestDto.getAmount(),
                    paymentRequestDto.getPhoneNumber(),
                    paymentRequestDto.getName(),
                    paymentRequestDto.getEmail(),
                    paymentRequestDto.getPaymentGatewayType());

            System.out.println("✅ Payment link generated successfully");
            return result;
        } catch (Exception e) {
            System.err.println("❌ Payment generation failed: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
