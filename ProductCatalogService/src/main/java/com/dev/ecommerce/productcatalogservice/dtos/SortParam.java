package com.dev.ecommerce.productcatalogservice.dtos;

public class SortParam {
    private String paramName;
    private String order;

    // Getters and Setters
    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
