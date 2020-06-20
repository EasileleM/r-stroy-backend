package com.example.rstroybackend.service;

import com.example.rstroybackend.dto.CreateOrderDto;
import com.example.rstroybackend.dto.ProductIdDto;
import com.example.rstroybackend.dto.StashedProductDto;
import com.example.rstroybackend.entity.Order;
import com.example.rstroybackend.entity.User;

import java.util.Set;

public interface UserService {
    User register(User user);

    Set<User> getAll();

    User findByEmail(String email);

    User findByPhoneNumber(String phoneNumber);

    User findById(Long id);

    User updateUserFavorites(Set<ProductIdDto> favoritesProductsIds, Long id);

    User updateUserCart(Set<StashedProductDto> cartProducts, Long id);

    Order createOrder(CreateOrderDto order, Long id);

    void cancelOrder(Order order, Long id);

    void delete(Long id);
}
