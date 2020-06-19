package com.example.rstroybackend.service;

import com.example.rstroybackend.entity.ProductType;

import java.util.Set;

public interface ProductTypesService {
    ProductType findById(Long id);
    ProductType findByName(String name);
    Set<ProductType> findAll();
    ProductType create(ProductType type);
    ProductType update(ProductType type);
    void delete(Long id);
}
