package com.dev.ecommerce.productcatalogservice.services;

import com.dev.ecommerce.productcatalogservice.dtos.UserDTO;
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
    public StorageProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    @Override
    public Product getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty() || (State.INACTIVE).equals(product.get().getState())){
            return null;
        } 
        return product.get();
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().filter(data -> (State.ACTIVE).equals(data.getState())).toList();
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
        return productRepository.save(input);
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
}
