package com.example.rstroybackend.repo;

import com.example.rstroybackend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order, Long> {
}
