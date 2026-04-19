package com.dev.ecommerce.productcatalogservice.dtos;

import java.util.List;

public class SearchRequestDto {
    private String query;
    private int page;
    private int size;
    private List<SortParam> sortParams;

    // Getters and Setters
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<SortParam> getSortParams() {
        return sortParams;
    }

    public void setSortParams(List<SortParam> sortParams) {
        this.sortParams = sortParams;
    }
}
