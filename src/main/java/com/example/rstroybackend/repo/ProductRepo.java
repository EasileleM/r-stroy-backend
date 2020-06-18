package com.example.rstroybackend.repo;

import com.example.rstroybackend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

import java.math.BigDecimal;

public interface ProductRepo extends JpaRepository<Product, Long> {
    @Query("select distinct p from Product p left join p.types types where upper(p.name) like '%' || upper(:name) || '%' and (:maxPrice is null or p.price <= :maxPrice) and (:minPrice is null or p.price >= :minPrice) and ((:typeNames) is null or types.name in (:typeNames))")
    Page<Product> findByFilters(String name, List<String> typeNames, BigDecimal maxPrice, BigDecimal minPrice, Pageable pageable);

    @Override
    Page<Product> findAll(Pageable pageable);

    @Query("select max(price) from Product")
    BigDecimal findByMaxPrice();

    @Query("select min(price) from Product")
    BigDecimal findByMinPrice();
}
