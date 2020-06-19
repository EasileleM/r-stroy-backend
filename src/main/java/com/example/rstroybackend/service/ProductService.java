package com.example.rstroybackend.service;

import com.example.rstroybackend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Set;

public interface ProductService {
    Page<Product> findByFilters(String name, Iterable<String> types, Integer maxPrice, Integer minPrice, Pageable pageable);
    Product findById(Long id);
    Set<Product> findByIds(Iterable<Long> ids);
    Product create(Product product);
    Product update(Product product);
    BigDecimal getMaxPrice();
    BigDecimal getMinPrice();
    void delete(Long id);
}
