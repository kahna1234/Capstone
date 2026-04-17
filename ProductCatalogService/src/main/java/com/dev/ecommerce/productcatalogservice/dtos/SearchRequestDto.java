package com.dev.ecommerce.productcatalogservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class SearchRequestDto {
    private String query;
    private int page;
    private int size;
    private List<SortParam> sortParams;
}
