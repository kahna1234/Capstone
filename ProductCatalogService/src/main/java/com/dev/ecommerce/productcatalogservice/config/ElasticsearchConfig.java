package com.dev.ecommerce.productcatalogservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;

import jakarta.annotation.PostConstruct;

@Configuration
public class ElasticsearchConfig {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchConfig.class);

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @PostConstruct
    public void createIndexWithSettings() {
        try {
            IndexOperations indexOperations = elasticsearchTemplate.indexOps(com.dev.ecommerce.productcatalogservice.documents.ProductDocument.class);

            if (!indexOperations.exists()) {
                indexOperations.create();
                indexOperations.putMapping();
                logger.info("Elasticsearch index 'products' created successfully");
            } else {
                logger.info("Elasticsearch index 'products' already exists");
            }
        } catch (Exception e) {
            logger.error("Failed to create Elasticsearch index", e);
        }
    }
}
