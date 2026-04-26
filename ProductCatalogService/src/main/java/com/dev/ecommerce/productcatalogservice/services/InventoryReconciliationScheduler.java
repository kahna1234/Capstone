package com.dev.ecommerce.productcatalogservice.services;

import com.dev.ecommerce.productcatalogservice.dtos.InventorySnapshotDto;
import com.dev.ecommerce.productcatalogservice.models.Product;
import com.dev.ecommerce.productcatalogservice.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class InventoryReconciliationScheduler {

    private static final Logger logger = LoggerFactory.getLogger(InventoryReconciliationScheduler.class);

    private final RestTemplate restTemplate;
    private final ProductRepository productRepository;

    @Value("${inventory.reconciliation.enabled:true}")
    private boolean reconciliationEnabled;

    public InventoryReconciliationScheduler(RestTemplate restTemplate, ProductRepository productRepository) {
        this.restTemplate = restTemplate;
        this.productRepository = productRepository;
    }

    @Scheduled(cron = "${inventory.reconciliation.cron:0 */5 * * * *}")
    public void reconcileInventoryQuantities() {
        if (!reconciliationEnabled) {
            return;
        }

        try {
            InventorySnapshotDto[] snapshots = restTemplate.getForObject(
                    "http://inventoryservice/inventory",
                    InventorySnapshotDto[].class
            );

            if (snapshots == null || snapshots.length == 0) {
                return;
            }

            Map<Long, InventorySnapshotDto> inventoryByProductId = Arrays.stream(snapshots)
                    .filter(snapshot -> snapshot.getProductId() != null)
                    .collect(Collectors.toMap(
                            InventorySnapshotDto::getProductId,
                            Function.identity(),
                            (left, right) -> right
                    ));

            int repairedProducts = 0;
            for (Product product : productRepository.findAll()) {
                InventorySnapshotDto snapshot = inventoryByProductId.get(product.getId());
                if (snapshot == null) {
                    continue;
                }

                Integer sourceQuantity = snapshot.getQuantity() != null ? snapshot.getQuantity() : 0;
                Integer currentQuantity = product.getInventoryQuantity() != null ? product.getInventoryQuantity() : 0;

                if (!sourceQuantity.equals(currentQuantity)) {
                    product.setInventoryQuantity(sourceQuantity);
                    productRepository.save(product);
                    repairedProducts++;
                }
            }

            if (repairedProducts > 0) {
                logger.info("Inventory reconciliation repaired {} product inventory copies", repairedProducts);
            }
        } catch (Exception e) {
            logger.error("Inventory reconciliation job failed", e);
        }
    }
}
