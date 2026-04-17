package com.dev.ecommerce.orderservice.clients;

import com.dev.ecommerce.orderservice.dtos.OrderDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducerClient {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendOrderConfirmation(OrderDto orderDto) {
        try {
            String message = objectMapper.writeValueAsString(orderDto);
            kafkaTemplate.send("OrderCreated", message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send order confirmation", e);
        }
    }
}
