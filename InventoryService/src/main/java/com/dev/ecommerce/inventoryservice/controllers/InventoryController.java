package com.dev.ecommerce.inventoryservice.controllers;

import com.dev.ecommerce.inventoryservice.dtos.InventoryDto;
import com.dev.ecommerce.inventoryservice.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    /** GET /inventory — list all inventory records */
    @GetMapping
    public ResponseEntity<List<InventoryDto>> getAllInventory() {
        List<InventoryDto> list = inventoryService.getAllInventory();
        return ResponseEntity.ok(list);
    }

    /** GET /inventory/{productId} — stock for a specific product */
    @GetMapping("/{productId}")
    public ResponseEntity<InventoryDto> getInventoryByProductId(@PathVariable Long productId) {
        InventoryDto dto = inventoryService.getInventoryByProductId(productId);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    /** GET /inventory/{productId}/check?quantity=X — check if sufficient inventory available */
    @GetMapping("/{productId}/check")
    public ResponseEntity<Map<String, Boolean>> checkInventoryAvailability(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        
        InventoryDto dto = inventoryService.getInventoryByProductId(productId);
        boolean available = dto != null && dto.getQuantity() >= quantity;
        
        return ResponseEntity.ok(Map.of("available", available));
    }

    /**
     * PUT /inventory/{productId} — admin: manually set stock quantity
     * Request body: { "quantity": 50 }
     */
    @PutMapping("/{productId}")
    public ResponseEntity<InventoryDto> updateInventory(
            @PathVariable Long productId,
            @RequestBody Map<String, Integer> body) {

        Integer quantity = body.get("quantity");
        if (quantity == null || quantity < 0) {
            return ResponseEntity.badRequest().build();
        }

        InventoryDto updated = inventoryService.updateInventory(productId, quantity);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }
}
