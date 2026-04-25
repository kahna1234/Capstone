package com.dev.ecommerce.paymentservice.strategies;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Razorpay implements PaymentGateway{
    @Autowired
    private RazorpayClient razorypayClient;

    @Value("${frontend.baseUrl}")
    private String frontendBaseUrl;

    @Override
    public String generatePaymentLink(String orderId,
                                      Long amount,
                                      String phoneNumber,
                                      String name,
                                      String email) throws RazorpayException {

        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount",amount*100);
        paymentLinkRequest.put("currency","INR");
//        paymentLinkRequest.put("accept_partial",true);
//        paymentLinkRequest.put("first_min_partial_amount",100);
        paymentLinkRequest.put("expire_by",System.currentTimeMillis() + 10*60*1000); //expiry is 10 min
        paymentLinkRequest.put("reference_id", orderId.toString());
        paymentLinkRequest.put("description","Payment request for Payment service class on Scaler");

        JSONObject customer = new JSONObject();
        customer.put("name", name);
        customer.put("contact",phoneNumber);
        customer.put("email",email);

        paymentLinkRequest.put("customer",customer);

        JSONObject notify = new JSONObject();
        notify.put("sms",true);
        notify.put("email",true);

        paymentLinkRequest.put("notify", notify);
        paymentLinkRequest.put("reminder_enable",true);

        JSONObject notes = new JSONObject();
        notes.put("policy_name","Jeevan Bima");

        paymentLinkRequest.put("notes",notes);

        paymentLinkRequest.put("callback_url", getFrontendUrl() + "/#payment-success?orderId=" + orderId);
        paymentLinkRequest.put("callback_method","get");

        PaymentLink payment = razorypayClient.paymentLink.create(paymentLinkRequest);
        return payment.get("short_url");
    }

    private String getFrontendUrl() {
        return frontendBaseUrl.endsWith("/")
                ? frontendBaseUrl.substring(0, frontendBaseUrl.length() - 1)
                : frontendBaseUrl;
    }
}
