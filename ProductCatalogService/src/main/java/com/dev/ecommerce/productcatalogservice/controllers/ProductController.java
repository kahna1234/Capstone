package com.dev.ecommerce.productcatalogservice.controllers;

import com.dev.ecommerce.productcatalogservice.dtos.ProductDTO;
import com.dev.ecommerce.productcatalogservice.models.Product;
import com.dev.ecommerce.productcatalogservice.services.IProductService;
import com.dev.ecommerce.productcatalogservice.services.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/products")
public class ProductController {
    IProductService productService;
    @Autowired
    ISearchService searchService;


    public ProductController(IProductService iProductService){
        this.productService = iProductService;
    }


    @PostMapping()
    ProductDTO createProduct(@RequestBody ProductDTO product){
        Product product1 = productService.createProduct(product.convertToProduct());
        if(product1 != null){
            return product1.convert();
        } else {
            throw new IllegalArgumentException("Product Already exists");
        }

    }

    @GetMapping("/{id}")
    ResponseEntity<ProductDTO> getProductById(@PathVariable Long id){
        if(id < 1){
            throw new IllegalArgumentException("Invalid Product Id (zero or negative)");
        }
        Product product = productService.getProductById(id);

        if(product == null){
            throw new IllegalArgumentException("ProductId doesn't exist");
        }


        ProductDTO productDTO = product.convert();

        return new ResponseEntity<>(productDTO,HttpStatus.OK);
    }

    @GetMapping()
    ResponseEntity<ProductDTO[]> getAllProducts(){
        List<Product> productList = productService.getAllProducts();
        ProductDTO[] productDTOarr = productList.stream().map(Product::convert).toArray(ProductDTO[]::new);
        return new ResponseEntity<>(productDTOarr,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    ResponseEntity<ProductDTO> replaceProduct(@RequestBody ProductDTO productDto, @PathVariable Long id){
//        Product product = fakestoreProductDto.from(fakestoreProductDto);
        Product product = productDto.convertToProduct();
        Product product1 = productService.replaceProduct(product,id);
        if(product1 == null){
            throw new IllegalArgumentException("No Product Found");
        }
        ProductDTO productDTO = product1.convert();
        return new ResponseEntity<>(productDTO,HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    ResponseEntity<Boolean> deleteProduct(@PathVariable Long id){
        boolean isDeleted = false;
        isDeleted =  productService.deleteProduct(id);
        if(isDeleted){
            return new ResponseEntity<>(isDeleted,HttpStatus.OK);
        }
        return new ResponseEntity<>(isDeleted,HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{productId}/{userId}")
    public ProductDTO getProductDetailsBasedOnUserRole(@PathVariable Long productId, @PathVariable Long userId) {
        System.out.println("Call reaching this api");
        Product product = productService.getProductBasedOnUserScope(productId, userId);
        return null;
    }

    @GetMapping("/search")
    public ResponseEntity<ProductDTO[]> searchProducts(@RequestParam String q) {
        Page<Product> searchResults = searchService.searchProducts(q, 0, 20, null);
        ProductDTO[] productDTOarr = searchResults.getContent().stream().map(Product::convert).toArray(ProductDTO[]::new);
        return new ResponseEntity<>(productDTOarr, HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ProductDTO[]> getProductsByCategory(@PathVariable Long categoryId) {
        if (categoryId < 1) {
            throw new IllegalArgumentException("Invalid Category Id (zero or negative)");
        }
        List<Product> products = productService.getProductsByCategory(categoryId);
        ProductDTO[] productDTOarr = products.stream().map(Product::convert).toArray(ProductDTO[]::new);
        return new ResponseEntity<>(productDTOarr, HttpStatus.OK);
    }
}
