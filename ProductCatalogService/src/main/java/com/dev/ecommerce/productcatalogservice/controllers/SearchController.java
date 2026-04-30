package com.dev.ecommerce.productcatalogservice.controllers;

import com.dev.ecommerce.productcatalogservice.documents.ProductDocument;
import com.dev.ecommerce.productcatalogservice.dtos.SearchRequestDto;
import com.dev.ecommerce.productcatalogservice.models.Product;
import com.dev.ecommerce.productcatalogservice.services.ElasticsearchSearchService;
import com.dev.ecommerce.productcatalogservice.services.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/search")
public class SearchController {
    @Autowired
    private ISearchService searchService;
    
    @Autowired
    private ElasticsearchSearchService elasticsearchSearchService;
    
    @PostMapping
    public Page<Product> search(@RequestBody SearchRequestDto searchRequestDto){
        return searchService.searchProducts(searchRequestDto.getQuery(),
                searchRequestDto.getPage(),
                searchRequestDto.getSize(),
                searchRequestDto.getSortParams());
    }
    
    @GetMapping("/autocomplete")
    public List<ProductDocument> autocomplete(@RequestParam String query) {
        return elasticsearchSearchService.autocompleteSearch(query);
    }
    
    @GetMapping("/fuzzy")
    public List<ProductDocument> fuzzySearch(@RequestParam String query) {
        return elasticsearchSearchService.fuzzySearch(query);
    }
}



