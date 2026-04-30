package com.dev.ecommerce.productcatalogservice.services;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;
import com.dev.ecommerce.productcatalogservice.documents.ProductDocument;
import com.dev.ecommerce.productcatalogservice.repositories.ProductElasticsearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElasticsearchSearchService {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchSearchService.class);

    @Autowired
    private ProductElasticsearchRepository productElasticsearchRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    /**
     * Autocomplete search - matches against name.autocomplete field
     */
    public List<ProductDocument> autocompleteSearch(String query) {
        logger.info("Autocomplete search for query: {}", query);
        
        Query matchQuery = MatchQuery.of(m -> m
                .field("name.autocomplete")
                .query(query)
        )._toQuery();

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(matchQuery)
                .build();

        SearchHits<ProductDocument> searchHits = elasticsearchOperations.search(nativeQuery, ProductDocument.class);
        
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    /**
     * Fuzzy multi-match search - searches across name and description with fuzziness
     */
    public List<ProductDocument> fuzzySearch(String query) {
        logger.info("Fuzzy search for query: {}", query);
        
        Query multiMatchQuery = MultiMatchQuery.of(m -> m
                .query(query)
                .fields("name^3", "description")
                .fuzziness("AUTO")
        )._toQuery();

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(multiMatchQuery)
                .build();

        SearchHits<ProductDocument> searchHits = elasticsearchOperations.search(nativeQuery, ProductDocument.class);
        
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    /**
     * Save product document to Elasticsearch
     */
    public ProductDocument saveToElastic(ProductDocument productDocument) {
        logger.info("Saving product to Elasticsearch: {}", productDocument.getName());
        return productElasticsearchRepository.save(productDocument);
    }

    /**
     * Delete product document from Elasticsearch
     */
    public void deleteFromElastic(String id) {
        logger.info("Deleting product from Elasticsearch with id: {}", id);
        productElasticsearchRepository.deleteById(id);
    }

    /**
     * Get all products from Elasticsearch
     */
    public List<ProductDocument> getAllProducts() {
        return (List<ProductDocument>) productElasticsearchRepository.findAll();
    }
}
