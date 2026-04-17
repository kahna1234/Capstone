package com.dev.ecommerce.productcatalogservice.controllers;

import com.dev.ecommerce.productcatalogservice.dtos.SearchRequestDto;
import com.dev.ecommerce.productcatalogservice.models.Product;
import com.dev.ecommerce.productcatalogservice.services.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/search")
public class SearchController {
    @Autowired
    private ISearchService searchService;
    @PostMapping
    public Page<Product> search(@RequestBody SearchRequestDto searchRequestDto){
        return searchService.searchProducts(searchRequestDto.getQuery(),
                searchRequestDto.getPage(),
                searchRequestDto.getSize(),
                searchRequestDto.getSortParams());
    }
}



