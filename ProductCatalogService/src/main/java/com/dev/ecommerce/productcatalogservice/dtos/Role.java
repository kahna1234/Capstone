package com.dev.ecommerce.productcatalogservice.dtos;

import com.dev.ecommerce.productcatalogservice.models.BaseModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Role extends BaseModel {
    private String name;
}
