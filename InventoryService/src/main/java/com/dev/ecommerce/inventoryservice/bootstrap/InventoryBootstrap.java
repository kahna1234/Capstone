package com.dev.ecommerce.inventoryservice.bootstrap;

import com.dev.ecommerce.inventoryservice.entities.Inventory;
import com.dev.ecommerce.inventoryservice.repositories.InventoryRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

/**
 * Ensures InventoryService has initial stock rows for seeded products.
 * Without this, order creation fails with "Insufficient inventory".
 */
@Component
public class InventoryBootstrap implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(InventoryBootstrap.class);

    private final InventoryRepository inventoryRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${bootstrap.inventory.enabled:true}")
    private boolean enabled;

    @Value("${product.catalog.base-url:http://api-gateway:8088}")
    private String productCatalogBaseUrl;

    @Value("${bootstrap.inventory.max-wait-seconds:60}")
    private long maxWaitSeconds;

    public InventoryBootstrap(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!enabled) {
            log.info("Inventory bootstrap disabled.");
            return;
        }

        String url = productCatalogBaseUrl.replaceAll("/+$", "") + "/products";
        ProductSnapshot[] products = fetchProductsWithRetry(url);
        if (products == null || products.length == 0) {
            log.warn("Inventory bootstrap: no products fetched from {}. Inventory will remain unchanged.", url);
            return;
        }

        int created = 0;
        for (ProductSnapshot p : products) {
            if (p == null || p.id == null) continue;

            Optional<Inventory> existing = inventoryRepository.findByProductId(p.id);
            if (existing.isPresent()) continue;

            Inventory inv = new Inventory();
            inv.setProductId(p.id);
            inv.setProductName(p.name);
            inv.setQuantity(p.inventoryQuantity != null ? p.inventoryQuantity : 0);
            inv.setReservedQty(0);
            inv.setCreatedAt(new Date());
            inv.setLastUpdatedAt(new Date());

            inventoryRepository.save(inv);
            created++;
        }

        log.info("Inventory bootstrap complete. Fetched {} products, created {} inventory rows.", products.length, created);
    }

    private ProductSnapshot[] fetchProductsWithRetry(String url) {
        long deadline = System.currentTimeMillis() + Duration.ofSeconds(maxWaitSeconds).toMillis();
        int attempt = 0;

        while (System.currentTimeMillis() < deadline) {
            attempt++;
            try {
                ResponseEntity<ProductSnapshot[]> resp =
                        restTemplate.getForEntity(url, ProductSnapshot[].class);
                ProductSnapshot[] body = resp.getBody();
                if (body != null) {
                    log.info("Fetched {} products from {} (attempt {}).", body.length, url, attempt);
                    return body;
                }
            } catch (Exception e) {
                log.warn("Failed fetching products from {} (attempt {}): {}", url, attempt, e.getMessage());
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        return null;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class ProductSnapshot {
        public Long id;
        public String name;
        public Integer inventoryQuantity;
    }
}

