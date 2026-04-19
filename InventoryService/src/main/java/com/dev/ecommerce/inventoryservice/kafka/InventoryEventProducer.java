package com.dev.ecommerce.inventoryservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Publishes an "inventory-updated" event to Kafka so that ProductCatalogService
 * can keep its denormalized product.inventoryQuantity field in sync.
 */
@Service
public class InventoryEventProducer {

    private static final String TOPIC = "inventory-updated";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void publishInventoryUpdated(Long productId, Integer newQuantity) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("productId", productId);
            payload.put("newQuantity", newQuantity);
            String message = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(TOPIC, String.valueOf(productId), message);
            System.out.println("[InventoryEventProducer] Published inventory-updated: productId="
                    + productId + " newQuantity=" + newQuantity);
        } catch (Exception e) {
            System.err.println("[InventoryEventProducer] Failed to publish inventory-updated: " + e.getMessage());
        }
    }
}
