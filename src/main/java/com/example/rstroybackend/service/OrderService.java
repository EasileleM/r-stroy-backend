package com.example.rstroybackend.service;

import com.example.rstroybackend.entity.Order;
import com.example.rstroybackend.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<Order> findAll(Pageable pageable);
    Order findById(Long id);
    Order updateOrderStatus(OrderStatus status, Long id);
}
