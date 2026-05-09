package com.dev.ecommerce.productcatalogservice.services;

import com.dev.ecommerce.productcatalogservice.models.Product;

import java.util.List;

public interface IProductService {
    Product getProductById(Long id);
    List<Product> getAllProducts();
    Product createProduct(Product input);
    Product replaceProduct(Product product, Long id);
    boolean deleteProduct(Long id);
    Product getProductBasedOnUserScope(Long productId, Long userId);
    List<Product> getProductsByCategory(Long categoryId);
}
