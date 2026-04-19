package com.dev.ecommerce.inventoryservice.kafka;

import com.dev.ecommerce.inventoryservice.entities.Inventory;
import com.dev.ecommerce.inventoryservice.repositories.InventoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dev.ecommerce.inventoryservice.dtos.OrderDto;
import com.dev.ecommerce.inventoryservice.dtos.OrderItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * Kafka consumers for InventoryService.
 *
 * Listens to:
 *   1. "product-created"  → auto-creates an Inventory record for the new product
 *   2. "OrderCreated"     → deducts stock when an order is CONFIRMED (Stripe payment success)
 */
@Component
public class InventoryKafkaConsumer {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryEventProducer inventoryEventProducer;

    @Autowired
    private ObjectMapper objectMapper;

    // -------------------------------------------------------------------------
    // 1. Auto-create inventory record when a product is created
    // -------------------------------------------------------------------------
    @KafkaListener(topics = "product-created", groupId = "inventory-service-group")
    public void onProductCreated(String message) {
        try {
            System.out.println("[InventoryKafkaConsumer] Received product-created: " + message);

            @SuppressWarnings("unchecked")
            Map<String, Object> event = objectMapper.readValue(message, Map.class);
            Long productId = Long.valueOf(event.get("productId").toString());
            String productName = (String) event.get("productName");
            Integer initialQty = event.get("initialQuantity") != null
                    ? Integer.valueOf(event.get("initialQuantity").toString())
                    : 0;

            // Avoid duplicates
            if (inventoryRepository.findByProductId(productId).isPresent()) {
                System.out.println("[InventoryKafkaConsumer] Inventory already exists for productId=" + productId);
                return;
            }

            Inventory inventory = new Inventory();
            inventory.setProductId(productId);
            inventory.setProductName(productName);
            inventory.setQuantity(initialQty);
            inventory.setReservedQty(0);
            inventory.setCreatedAt(new Date());
            inventory.setLastUpdatedAt(new Date());
            inventoryRepository.save(inventory);

            System.out.println("[InventoryKafkaConsumer] Created inventory record for productId=" + productId
                    + " with quantity=" + initialQty);

            // Publish back so ProductCatalogService syncs its denormalized field
            inventoryEventProducer.publishInventoryUpdated(productId, initialQty);

        } catch (Exception e) {
            System.err.println("[InventoryKafkaConsumer] Failed to process product-created: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // 2. Deduct stock when an order is confirmed (same topic EmailService uses)
    // -------------------------------------------------------------------------
    @KafkaListener(topics = "OrderCreated", groupId = "inventory-service-group")
    public void onOrderConfirmed(String message) {
        try {
            System.out.println("[InventoryKafkaConsumer] Received OrderCreated: " + message);
            OrderDto orderDto = objectMapper.readValue(message, OrderDto.class);

            if (orderDto.getItems() == null || orderDto.getItems().isEmpty()) {
                System.out.println("[InventoryKafkaConsumer] No items in order, skipping.");
                return;
            }

            for (OrderItemDto item : orderDto.getItems()) {
                Optional<Inventory> optInv = inventoryRepository.findByProductId(item.getProductId());
                if (optInv.isEmpty()) {
                    System.err.println("[InventoryKafkaConsumer] No inventory found for productId=" + item.getProductId());
                    continue;
                }

                Inventory inventory = optInv.get();
                int deduct = item.getQuantity() != null ? item.getQuantity() : 0;

                if (inventory.getQuantity() < deduct) {
                    System.err.println("[InventoryKafkaConsumer] Insufficient stock for productId="
                            + item.getProductId() + " (available=" + inventory.getQuantity()
                            + ", requested=" + deduct + "). Allowing order but logging warning.");
                    inventory.setQuantity(0);
                } else {
                    inventory.setQuantity(inventory.getQuantity() - deduct);
                }

                inventory.setLastUpdatedAt(new Date());
                inventoryRepository.save(inventory);

                System.out.println("[InventoryKafkaConsumer] Deducted " + deduct
                        + " units for productId=" + item.getProductId()
                        + " → remaining=" + inventory.getQuantity());

                // Publish inventory-updated so ProductCatalogService syncs its field
                inventoryEventProducer.publishInventoryUpdated(item.getProductId(), inventory.getQuantity());
            }

        } catch (Exception e) {
            System.err.println("[InventoryKafkaConsumer] Failed to process OrderCreated: " + e.getMessage());
        }
    }
}
