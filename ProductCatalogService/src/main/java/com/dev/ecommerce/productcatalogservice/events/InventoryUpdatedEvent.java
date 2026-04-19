package com.dev.ecommerce.productcatalogservice.events;

/**
 * Event consumed from "inventory-updated" Kafka topic.
 * Published by InventoryService whenever stock changes.
 * ProductCatalogService uses this to keep product.inventoryQuantity in sync.
 */
public class InventoryUpdatedEvent {
    private Long productId;
    private Integer newQuantity;

    // No-arg constructor
    public InventoryUpdatedEvent() {
    }

    // All-args constructor
    public InventoryUpdatedEvent(Long productId, Integer newQuantity) {
        this.productId = productId;
        this.newQuantity = newQuantity;
    }

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getNewQuantity() {
        return newQuantity;
    }

    public void setNewQuantity(Integer newQuantity) {
        this.newQuantity = newQuantity;
    }
}
