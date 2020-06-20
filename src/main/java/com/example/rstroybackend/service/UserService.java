package com.example.rstroybackend.service;

import com.example.rstroybackend.dto.CreateOrderRequestDto;
import com.example.rstroybackend.dto.ProductIdDto;
import com.example.rstroybackend.dto.StashedProductDto;
import com.example.rstroybackend.dto.UpdateUserRequestDto;
import com.example.rstroybackend.entity.Order;
import com.example.rstroybackend.entity.User;

import java.util.Set;

public interface UserService {
    User register(User user);

    Set<User> getAll();

    User findByEmail(String email);

    User findByPhoneNumber(String phoneNumber);

    User findById(Long id);

    User updateFavorites(Set<ProductIdDto> favoritesProductsIds, Long id);

    User updateCart(Set<StashedProductDto> cartProducts, Long id);

    Order createOrder(CreateOrderRequestDto order, Long id);

    User update(UpdateUserRequestDto updateUserRequestDto, Long id);

    void cancelOrder(Long orderId, Long id);

    void delete(Long id);
}
