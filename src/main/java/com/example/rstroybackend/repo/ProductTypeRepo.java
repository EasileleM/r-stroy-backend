package com.example.rstroybackend.repo;

import com.example.rstroybackend.entity.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductTypeRepo extends JpaRepository<ProductType, Long> {
    Optional<ProductType> findByName(String name);
}
