package com.example.rstroybackend.repository;

import com.example.rstroybackend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCaseAndAndTypes_NameIn(String name, List<String> typeNames);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByTypes_NameIn(List<String> typeNames);
}
