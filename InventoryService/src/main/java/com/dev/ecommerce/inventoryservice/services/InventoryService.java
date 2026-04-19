package com.dev.ecommerce.inventoryservice.services;

import com.dev.ecommerce.inventoryservice.dtos.InventoryDto;
import com.dev.ecommerce.inventoryservice.entities.Inventory;
import com.dev.ecommerce.inventoryservice.kafka.InventoryEventProducer;
import com.dev.ecommerce.inventoryservice.repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryEventProducer inventoryEventProducer;

    public List<InventoryDto> getAllInventory() {
        return inventoryRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public InventoryDto getInventoryByProductId(Long productId) {
        Optional<Inventory> opt = inventoryRepository.findByProductId(productId);
        return opt.map(this::toDto).orElse(null);
    }

    /**
     * Admin endpoint: manually set the stock quantity for a product.
     * Publishes inventory-updated event so ProductCatalogService syncs.
     */
    public InventoryDto updateInventory(Long productId, Integer quantity) {
        Optional<Inventory> opt = inventoryRepository.findByProductId(productId);
        if (opt.isEmpty()) return null;

        Inventory inventory = opt.get();
        inventory.setQuantity(quantity);
        inventory.setLastUpdatedAt(new Date());
        inventoryRepository.save(inventory);

        // Sync ProductCatalogService via Kafka
        inventoryEventProducer.publishInventoryUpdated(productId, quantity);

        return toDto(inventory);
    }

    private InventoryDto toDto(Inventory inventory) {
        InventoryDto dto = new InventoryDto();
        dto.setId(inventory.getId());
        dto.setProductId(inventory.getProductId());
        dto.setProductName(inventory.getProductName());
        dto.setQuantity(inventory.getQuantity());
        dto.setReservedQty(inventory.getReservedQty());
        return dto;
    }
}
