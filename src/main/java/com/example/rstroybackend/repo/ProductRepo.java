package com.example.rstroybackend.repo;

import com.example.rstroybackend.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
    Page<Product> findByNameContainingIgnoreCaseAndAndTypes_NameIn(String name, List<String> typeNames, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Product> findByTypes_NameIn(List<String> typeNames, Pageable pageable);
    @Override
    Page<Product> findAll(Pageable pageable);

    @Query("select max(price) from Product")
    Integer findByMaxPrice();

    @Query("select min(price) from Product")
    Integer findByMinPrice();
}
