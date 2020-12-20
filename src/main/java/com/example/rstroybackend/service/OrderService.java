package com.example.rstroybackend.service;

import com.example.rstroybackend.entity.Order;
import com.example.rstroybackend.entity.User;
import com.example.rstroybackend.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    Page<Order> findAll(Pageable pageable);
    List<Order> findAll();
    Order findById(Long id);
    Order updateOrderStatus(OrderStatus status, Long id);
    User findUserByOrder(Long id);
    void validateCity(String city);
}
