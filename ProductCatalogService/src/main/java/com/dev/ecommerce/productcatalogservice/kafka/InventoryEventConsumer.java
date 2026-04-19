package com.dev.ecommerce.productcatalogservice.kafka;

import com.dev.ecommerce.productcatalogservice.events.InventoryUpdatedEvent;
import com.dev.ecommerce.productcatalogservice.models.Product;
import com.dev.ecommerce.productcatalogservice.repositories.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Kafka consumer for ProductCatalogService.
 *
 * Listens to:
 *   - "inventory-updated"  → syncs product.inventoryQuantity from InventoryService
 */
@Component
public class InventoryEventConsumer {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Consumed when InventoryService updates stock (admin update OR order-confirmed deduction).
     * Keeps product.inventoryQuantity in sync with the source of truth.
     */
    @KafkaListener(topics = "inventory-updated", groupId = "product-catalog-group")
    public void onInventoryUpdated(String message) {
        try {
            System.out.println("[InventoryEventConsumer] Received inventory-updated: " + message);
            InventoryUpdatedEvent event = objectMapper.readValue(message, InventoryUpdatedEvent.class);

            Optional<Product> optProduct = productRepository.findById(event.getProductId());
            if (optProduct.isPresent()) {
                Product product = optProduct.get();
                product.setInventoryQuantity(event.getNewQuantity());
                productRepository.save(product);
                System.out.println("[InventoryEventConsumer] Updated inventoryQuantity=" + event.getNewQuantity()
                        + " for productId=" + event.getProductId());
            } else {
                System.err.println("[InventoryEventConsumer] Product not found for productId=" + event.getProductId());
            }
        } catch (Exception e) {
            System.err.println("[InventoryEventConsumer] Failed to process inventory-updated: " + e.getMessage());
        }
    }
}
