package com.example.rstroybackend.service;

import com.example.rstroybackend.dto.*;
import com.example.rstroybackend.entity.Order;
import com.example.rstroybackend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Set;

public interface UserService {
    Page<User> findAll(Pageable pageable);

    User findByEmail(String email);

    User findByPhoneNumber(String phoneNumber);

    User findById(Long userId);

    User register(RegistrationRequestDto userDto);

    void activate(String activationCode);

    User update(UpdateCurrentUserRequestDto updateCurrentUserRequestDto, Long userId);

    User update(UpdateUserRequestDto updateUserRequestDto, Long userId);

    void delete(Long userId);

    User updateFavorites(Set<ProductIdDto> favoritesProductsIds, Long userId);

    User updateCart(Set<StashedProductDto> cartProducts, Long userId);

    Order createOrder(CreateOrderRequestDto order, Long userId);

    void cancelOrder(Long orderId, Long userId);

    void changeIsSubscribed(Boolean isSubscribed, Long userId);
}
