package com.dev.ecommerce.productcatalogservice.services;

import com.dev.ecommerce.productcatalogservice.dtos.SortParam;
import com.dev.ecommerce.productcatalogservice.models.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ISearchService {
    Page<Product> searchProducts(String query, int page, int size, List<SortParam> sortParams);
}
