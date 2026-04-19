package com.dev.ecommerce.productcatalogservice.events;

/**
 * Event published to "product-created" Kafka topic when a new product is saved.
 * InventoryService consumes this to auto-create an inventory record.
 */
public class ProductCreatedEvent {
    private Long productId;
    private String productName;
    private Integer initialQuantity; // forwarded from ProductDTO.inventoryQuantity

    // No-arg constructor
    public ProductCreatedEvent() {
    }

    // All-args constructor
    public ProductCreatedEvent(Long productId, String productName, Integer initialQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.initialQuantity = initialQuantity;
    }

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getInitialQuantity() {
        return initialQuantity;
    }

    public void setInitialQuantity(Integer initialQuantity) {
        this.initialQuantity = initialQuantity;
    }
}
