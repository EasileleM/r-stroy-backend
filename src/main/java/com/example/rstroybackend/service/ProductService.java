package com.example.rstroybackend.service;

import com.example.rstroybackend.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findByFilters(String name, List<String> types);
    Product findById(Long id);
    List<Product> findByIds(List<Long> ids);
    Product create(Product product);
    Product update(Product product);
    void delete(Long id);
}
