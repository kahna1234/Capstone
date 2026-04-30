package com.dev.ecommerce.productcatalogservice.repositories;

import com.dev.ecommerce.productcatalogservice.documents.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductElasticsearchRepository extends ElasticsearchRepository<ProductDocument, String> {
    List<ProductDocument> findByName(String name);
}
