package com.dev.ecommerce.productcatalogservice.services;

import com.dev.ecommerce.productcatalogservice.dtos.UserDTO;
import com.dev.ecommerce.productcatalogservice.documents.ProductDocument;
import com.dev.ecommerce.productcatalogservice.events.ProductCreatedEvent;
import com.dev.ecommerce.productcatalogservice.kafka.ProductEventProducer;
import com.dev.ecommerce.productcatalogservice.models.Product;
import com.dev.ecommerce.productcatalogservice.models.State;
import com.dev.ecommerce.productcatalogservice.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("storageProductService")
@Primary
public class StorageProductService implements IProductService{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ProductEventProducer productEventProducer;
    @Autowired
    private ElasticsearchSearchService elasticsearchSearchService;

    public StorageProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    @Override
    public Product getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty()){
            return null;
        }
        State productState = product.get().getState();
        // Check if product is active (handle both enum and ordinal comparisons)
        if(productState == null || 
           (productState.equals(State.INACTIVE) || productState.ordinal() == 1)){
            return null;
        }
        return product.get();
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        // Filter for active products - handle both enum and ordinal comparisons
        return products.stream().filter(data -> {
            State productState = data.getState();
            return productState != null && 
                   (productState.equals(State.ACTIVE) || 
                    (productState.ordinal() == 0)); // State.ACTIVE has ordinal 0
        }).toList();
    }

    @Override
    public Product createProduct(Product input) {
        //Optional<Product> optionalProduct = productRepository.findById(input.getId());
//        if(optionalProduct.isEmpty() || (State.INACTIVE).equals(optionalProduct.get().getState())){
//
//        }
//        return null;
        input.setId(null);
        Date date = new Date();
        if(input.getCreatedAt() == null){
            input.setCreatedAt(date);
        }
        if(input.getLastUpdatedAt() == null){
            input.setLastUpdatedAt(date);
        }
        if(input.getState() == null){
            input.setState(State.ACTIVE);
        }
        Product saved = productRepository.save(input);
        // Publish Kafka event so InventoryService creates an inventory record
        productEventProducer.publishProductCreated(
                new ProductCreatedEvent(saved.getId(), saved.getName(), saved.getInventoryQuantity())
        );
        // Save to Elasticsearch
        saveToElastic(saved);
        return saved;
    }

    @Override
    public Product replaceProduct(Product product, Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isPresent()){
            Product product1 = optionalProduct.get();
            if((State.ACTIVE).equals(product1.getState())){
                product.setId(id);
                product.setCreatedAt(product1.getCreatedAt());
                if(product.getLastUpdatedAt() == null){
                    product.setLastUpdatedAt(new Date());
                }
//                if(product.getCreatedAt() == null){
//                    product.setCreatedAt(new Date());
//                }
                productRepository.save(product);
                return product;
            }
        }
        return null;
    }

    @Override
    public boolean deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isPresent()){
            Product product = optionalProduct.get();
            if((State.ACTIVE).equals(product.getState())){
                product.setState(State.INACTIVE);
                productRepository.save(product);
                return true;
            }
        }
        return false;
    }
    @Override
    public Product getProductBasedOnUserScope(Long productId, Long userId) {

        Optional<Product> product = productRepository.findById(productId);

        /*
        check if product is listed or not
         */

        //product is not listed
        //call into the user service

        try {
            UserDTO userDto = restTemplate.getForEntity(
                    "http://UserAuthService/users/{userId}",
                    UserDTO.class,
                    userId).getBody();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        /*
        get list of ips of UserAuthService from eureka server
        using client side load balancing to call the UserAuthService
         */


        return null;
    }

    @Override
    public List<Product> getProductsByCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        // Filter for active products - handle both enum and ordinal comparisons
        return products.stream().filter(data -> {
            State productState = data.getState();
            return productState != null && 
                   (productState.equals(State.ACTIVE) || 
                    (productState.ordinal() == 0)); // State.ACTIVE has ordinal 0
        }).toList();
    }

    private void saveToElastic(Product product) {
        try {
            String brand = product.getCategory() != null ? product.getCategory().getName() : null;
            ProductDocument productDocument = new ProductDocument(
                    String.valueOf(product.getId()),
                    product.getName(),
                    product.getDescription(),
                    brand,
                    product.getPrice() != null ? product.getPrice() : 0.0
            );
            elasticsearchSearchService.saveToElastic(productDocument);
        } catch (Exception e) {
            // Log error but don't fail the main operation
            System.err.println("Failed to save product to Elasticsearch: " + e.getMessage());
        }
    }
}
