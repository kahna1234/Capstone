package com.dev.ecommerce.productcatalogservice.services;

import com.dev.ecommerce.productcatalogservice.clients.FakeStoreAPIClient;
import com.dev.ecommerce.productcatalogservice.dtos.FakestoreProductDto;
import com.dev.ecommerce.productcatalogservice.models.Product;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
@Service("fakestore")
public class FakestoreProductService implements IProductService{
    private final FakeStoreAPIClient fakeStoreAPIClient;
    public FakestoreProductService(FakeStoreAPIClient fakeStoreAPIClient){
        this.fakeStoreAPIClient = fakeStoreAPIClient;
    }

    @Override
    public Product getProductById(Long id) {
        ResponseEntity<FakestoreProductDto> entity = fakeStoreAPIClient.requestForEntity(HttpMethod.GET,"https://fakestoreapi.com/products/{id}",null,FakestoreProductDto.class,id);
        if(fakeStoreAPIClient.validateResponse(entity)){
            return entity.getBody().from(entity.getBody());
        }
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        ResponseEntity<FakestoreProductDto[]> entity = fakeStoreAPIClient.requestForEntity(HttpMethod.GET,"https://fakestoreapi.com/products",null,FakestoreProductDto[].class);
        if(entity.hasBody() && entity.getStatusCode().equals(HttpStatusCode.valueOf(200))){
            return Arrays.stream(entity.getBody()).map(dto->dto.from(dto)).toList();
        }
        return null;
    }

    @Override
    public Product createProduct(Product input) {
        return null;
    }

    @Override
    public Product replaceProduct(Product product, Long id){
        FakestoreProductDto fakestoreProductDto = product.convertToFakeStoreProduct();

        ResponseEntity<FakestoreProductDto> response = fakeStoreAPIClient.requestForEntity(HttpMethod.PUT,"https://fakestoreapi.com/products/{id}",fakestoreProductDto,FakestoreProductDto.class,id);

        if(fakeStoreAPIClient.validateResponse(response)){
            FakestoreProductDto fakestoreProductDto1 = response.getBody();
            return fakestoreProductDto1.from(fakestoreProductDto1);
        }

        return  null;
    }

    @Override
    public boolean deleteProduct(Long id) {
        return false;
    }

    @Override
    public Product getProductBasedOnUserScope(Long productId, Long userId) {
        return null;
    }
}
