package com.example.rstroybackend.service;

import com.example.rstroybackend.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    Page<Product> findByFilters(String name, List<String> types, Pageable pageable);
    Product findById(Long id);
    List<Product> findByIds(List<Long> ids);
    Product create(Product product);
    Product update(Product product);
    Integer getMaxPrice();
    Integer getMinPrice();
    void delete(Long id);
}
