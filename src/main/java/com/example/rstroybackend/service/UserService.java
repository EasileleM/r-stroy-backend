package com.example.rstroybackend.service;

import com.example.rstroybackend.dto.*;
import com.example.rstroybackend.entity.Order;
import com.example.rstroybackend.entity.User;

import java.util.Set;

public interface UserService {
    Set<User> findAll();

    User findByEmail(String email);

    User findByPhoneNumber(String phoneNumber);

    User findById(Long userId);

    User register(RegistrationRequestDto userDto);

    User update(UpdateUserRequestDto updateUserRequestDto, Long userId);

    void delete(Long userId);

    User updateFavorites(Set<ProductIdDto> favoritesProductsIds, Long userId);

    User updateCart(Set<StashedProductDto> cartProducts, Long userId);

    Order createOrder(CreateOrderRequestDto order, Long userId);

    void cancelOrder(Long orderId, Long userId);

    void changeIsSubscribed(Boolean isSubscribed, Long userId);
}
