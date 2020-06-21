package com.example.rstroybackend.service;

import com.example.rstroybackend.dto.*;
import com.example.rstroybackend.entity.Order;
import com.example.rstroybackend.entity.User;

import java.util.Set;

public interface UserService {
    User register(RegistrationRequestDto userDto);

    Set<User> findAll();

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
