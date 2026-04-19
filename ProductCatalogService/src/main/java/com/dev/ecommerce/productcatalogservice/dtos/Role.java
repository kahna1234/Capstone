package com.dev.ecommerce.productcatalogservice.dtos;

import com.dev.ecommerce.productcatalogservice.models.BaseModel;

public class Role extends BaseModel {
    private String name;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
