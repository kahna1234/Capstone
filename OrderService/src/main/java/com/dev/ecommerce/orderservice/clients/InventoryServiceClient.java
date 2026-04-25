package com.dev.ecommerce.orderservice.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Component
public class InventoryServiceClient {

    private final RestTemplate restTemplate;
    private final String inventoryServiceUrl;

    public InventoryServiceClient(RestTemplate restTemplate, 
                                   @Value("${inventory.service.url:http://api-gateway:8088}") String inventoryServiceUrl) {
        this.restTemplate = restTemplate;
        this.inventoryServiceUrl = inventoryServiceUrl;
    }

    /**
     * Check if inventory is available for a product
     * @param productId the product ID
     * @param requiredQuantity the quantity needed
     * @return true if inventory is available, false otherwise
     */
    public boolean checkInventory(Long productId, Integer requiredQuantity) {
        try {
            String url = inventoryServiceUrl + "/inventory/" + productId + "/check?quantity=" + requiredQuantity;
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Boolean available = (Boolean) response.getBody().get("available");
                return available != null && available;
            }
            return false;
        } catch (Exception e) {
            System.err.println("[InventoryServiceClient] Failed to check inventory for productId=" + productId + ": " + e.getMessage());
            return false;
        }
    }
}
