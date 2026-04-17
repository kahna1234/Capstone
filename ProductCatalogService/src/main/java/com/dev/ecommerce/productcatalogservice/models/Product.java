package com.dev.ecommerce.productcatalogservice.models;

import com.dev.ecommerce.productcatalogservice.dtos.CategoryDTO;
import com.dev.ecommerce.productcatalogservice.dtos.FakestoreProductDto;
import com.dev.ecommerce.productcatalogservice.dtos.ProductDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Product extends BaseModel{
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    @ManyToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    private Category category;
    public ProductDTO convert(){
        ProductDTO productDto = new ProductDTO();
        productDto.setId(this.getId());
        productDto.setName(this.getName());
        productDto.setDescription(this.getDescription());
        productDto.setPrice(this.getPrice());
        productDto.setImageUrl(this.getImageUrl());
        if(this.getCategory() != null) {
            CategoryDTO categoryDto = new CategoryDTO();
            categoryDto.setName(this.getCategory().getName());
            categoryDto.setId(this.getCategory().getId());
            categoryDto.setDescription(this.getCategory().getDescription());
            productDto.setCategory(categoryDto);
        }
        return productDto;
    }
    public FakestoreProductDto convertToFakeStoreProduct(){
        FakestoreProductDto fakeStoreProductDto = new FakestoreProductDto();
        fakeStoreProductDto.setId(this.getId());
        fakeStoreProductDto.setTitle(this.getName());
        fakeStoreProductDto.setPrice(this.getPrice());
        fakeStoreProductDto.setDescription(this.getDescription());
        fakeStoreProductDto.setImage(this.getImageUrl());
        if(this.getCategory() != null) {
            fakeStoreProductDto.setCategory(this.getCategory().getName());
        }
        return fakeStoreProductDto;
    }
}
