package com.dev.ecommerce.productcatalogservice.repositories;

import com.dev.ecommerce.productcatalogservice.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Override
    Optional<Product> findById(Long aLong);

    @Override
    List<Product> findAll();


    @Override
    Product save(Product entity);

    @Override
    void delete(Product entity);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
