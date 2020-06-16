package com.example.rstroybackend.service;

import com.example.rstroybackend.entity.ProductType;

import java.util.List;

public interface ProductTypesService {
    ProductType findById(Long id);
    ProductType findByName(String name);
    List<ProductType> findAll();
    ProductType create(ProductType type);
    ProductType update(ProductType type);
    void delete(Long id);
}
