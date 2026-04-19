package com.dev.ecommerce.productcatalogservice.controllers;
import com.dev.ecommerce.productcatalogservice.dtos.ProductDTO;
import com.dev.ecommerce.productcatalogservice.models.Product;
import com.dev.ecommerce.productcatalogservice.services.IProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/*
We use @WebMvcTest to test controllers because it loads only the MVC layer,
making tests faster, isolated, and focused on request/response handling.

@SpringBootTest loads the entire application, which is slow and turns
the test into a full integration test instead of a controller unit test.

 */

@WebMvcTest(ProductController.class)
public class ProductControllerMvcTests {

    /*
    Test the product controller web apis
     */

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IProductService productService;

    /*
    getAllProducts
    /products
     */

    /*
    In earlier scenario:
    we were testing method logic
    we were directly calling method from tests using object of controller


    GET /products

    List<ProductDTO> response
    [
        {
            "name" : "iPhone 14",
            "description" : "Apple iPhone 14 with A15 Bionic chip",
            "price" : 999.99
            ..
        },
        {
            "name" : "Samsung Galaxy S22",
            "description" : "Samsung Galaxy S22 with Exynos 2200",
            "price" : 899.99
            ..
        },
            ...
    ]


    POST /products
    "{
    "name" : "iPhone 14",
    "description" : "Apple iPhone 14 with A15 Bionic chip",
    "price" : 999.99
    }"
     gets converted into a ProductDTO




     */

    @Test
    public void testGetAllProducts_RunSuccessfully() throws Exception {
        //mockMvc.perform(get("/products")).andExpect(status().isOk());

        Product product = new Product();
        product.setId(1L);
        product.setName("iPhone 14");
        product.setDescription("Apple iPhone 14 with A15 Bionic chip");
        product.setPrice(999.99);


        List<Product> products = new ArrayList<>();

        products.add(product);

        when(productService.getAllProducts()).thenReturn(products);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setName("iPhone 14");
        productDTO.setDescription("Apple iPhone 14 with A15 Bionic chip");
        productDTO.setPrice(999.99);

        List<ProductDTO> productDTOS = new ArrayList<>();
        productDTOS.add(productDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String response = objectMapper.writeValueAsString(productDTOS);


        mockMvc.perform(get("/products")).
                andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    /*
    In previous types of tests, we were testing logic of methods,
    that's why we actually used the product controller and called the methods
    to run the actual code
     */



    /*
    using mockmvc, we are not actually testing the methods
    rather we are just testing the endpoints, that's it
     */

    /*
     */

}
