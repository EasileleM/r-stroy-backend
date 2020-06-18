package com.example.rstroybackend.service;

import com.example.rstroybackend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    Page<Product> findByFilters(String name, List<String> types, Integer maxPrice, Integer minPrice, Pageable pageable);
    Product findById(Long id);
    List<Product> findByIds(List<Long> ids);
    Product create(Product product);
    Product update(Product product);
    BigDecimal getMaxPrice();
    BigDecimal getMinPrice();
    void delete(Long id);
}
