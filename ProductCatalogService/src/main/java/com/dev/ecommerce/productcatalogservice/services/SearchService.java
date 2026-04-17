package com.dev.ecommerce.productcatalogservice.services;

import com.dev.ecommerce.productcatalogservice.dtos.SortParam;
import com.dev.ecommerce.productcatalogservice.models.Product;
import com.dev.ecommerce.productcatalogservice.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SearchService implements ISearchService{
    @Autowired
    private ProductRepository productRepository;
    @Override
    public Page<Product> searchProducts(String query, int page, int size, List<SortParam> sortParams) {

        Sort sort = Sort.unsorted();

        if (sortParams != null && !sortParams.isEmpty()) {

            for (SortParam sortParam : sortParams) {

                if (sortParam.getParamName() == null || sortParam.getParamName().isEmpty()) {
                    continue;
                }

                if ("ASC".equalsIgnoreCase(sortParam.getOrder())) {
                    sort = sort.and(Sort.by(sortParam.getParamName()).ascending());
                } else {
                    sort = sort.and(Sort.by(sortParam.getParamName()).descending());
                }
            }
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        return productRepository.findByNameContainingIgnoreCase(query, pageable);
    }
}
