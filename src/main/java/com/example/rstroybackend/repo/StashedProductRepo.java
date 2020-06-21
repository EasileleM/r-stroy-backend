package com.example.rstroybackend.repo;

import com.example.rstroybackend.entity.StashedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface StashedProductRepo extends JpaRepository<StashedProduct, Long> {
    @Transactional
    @Modifying
    @Query("delete from StashedProduct where product_id = :id and order_id is null")
    void deleteCartItemsByProductId(Long id);
}
