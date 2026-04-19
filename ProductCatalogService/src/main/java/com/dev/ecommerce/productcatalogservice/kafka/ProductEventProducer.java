package com.dev.ecommerce.productcatalogservice.kafka;

import com.dev.ecommerce.productcatalogservice.events.ProductCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Publishes a "product-created" event to Kafka whenever a new product is saved.
 * InventoryService listens on this topic to auto-create an inventory record.
 */
@Service
public class ProductEventProducer {

    private static final String TOPIC = "product-created";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void publishProductCreated(ProductCreatedEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC, String.valueOf(event.getProductId()), message);
            System.out.println("[ProductEventProducer] Published product-created event for productId=" + event.getProductId());
        } catch (Exception e) {
            System.err.println("[ProductEventProducer] Failed to publish product-created event: " + e.getMessage());
        }
    }
}
