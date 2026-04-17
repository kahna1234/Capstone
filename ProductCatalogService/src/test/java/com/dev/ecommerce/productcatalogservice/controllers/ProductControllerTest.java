package com.dev.ecommerce.productcatalogservice.controllers;

import com.dev.ecommerce.productcatalogservice.dtos.ProductDTO;
import com.dev.ecommerce.productcatalogservice.models.Product;
import com.dev.ecommerce.productcatalogservice.services.StorageProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductControllerTest {

    /*
    This test file will contain tests for ProductController class
    Would you want product controller instance?
     */

    @Autowired
    private ProductController productController;

    @MockitoBean
    private StorageProductService productService;



    /*
    Happy case for getProductById
    Arrange, Act and Assert
    Let’s create a product and add a condition that when
    productService.getProductById is called with the id
    which we pass in the product controller method
    then it has to return this newly created product.

     */

    @Test
    public void testGetProductById_WithValidId_RunSuccessfully(){

        /*
        Arrange
         */
        Product product = new Product();
        product.setId(1L);
        product.setName("iPhone 14");
        product.setDescription("Apple iPhone 14 with A15 Bionic chip");
        product.setPrice(999.99);

        when(productService.getProductById(1L)).thenReturn(product);

        /*
        when I'll test getProductById of controller,
        behind the scenes, am I calling the real service?
         */

        /*
        Act step
         */

        ResponseEntity<ProductDTO> productDTOResponseEntity =
                productController.getProductById(1L);

        /*
        Assert step
         */
        assertNotNull(productDTOResponseEntity);
        assertNotNull(productDTOResponseEntity.getBody());
        assertEquals(product.getId(),
                productDTOResponseEntity.getBody().getId());
        assertEquals(product.getName(),
                productDTOResponseEntity.getBody().getName());
        assertEquals(product.getDescription(),
                productDTOResponseEntity.getBody().getDescription());

        verify(productService, times(1)).
                getProductById(1L);
        /*
        The verify() method is used in Mockito to check whether a mocked method was called,
        how many times, and with what arguments during a test.
         */
    }

    /*
    Sad scenario

    assertThrows(ExpectedException.class, () -> {
        // code that should throw the exception
    });

     */

    @Test
    public void testGetProductById_WithNegativeId_ThrowsIllegalArgumentException(){

        assertThrows(IllegalArgumentException.class, () -> productController.getProductById(-1L));

        verify(productService, times(0)).
                getProductById(-1L);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> productController.getProductById(-1L));

        assertEquals("Invalid Product Id (zero or negative)", exception.getMessage());



        //productController.getProductById(-1L); //get an exception
    }


}

/*
MockMvc tests are a way to test your Spring MVC controllers
without starting the full server.

They simulate HTTP requests (GET, POST, PUT…) and let you test:
- Controller endpoints
- Request validation
- Request/response structure
- HTTP status codes
- JSON serialization/deserialization
- Interactions with services (via mocks)


They are fast, lightweight, and focused.

 */