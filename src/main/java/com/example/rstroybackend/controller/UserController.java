package com.example.rstroybackend.controller;

import com.example.rstroybackend.dto.CreateOrderRequestDto;
import com.example.rstroybackend.dto.ProductIdDto;
import com.example.rstroybackend.dto.StashedProductDto;
import com.example.rstroybackend.dto.UpdateUserRequestDto;
import com.example.rstroybackend.entity.Order;
import com.example.rstroybackend.entity.User;
import com.example.rstroybackend.exceptions.ResourceNotFoundException;
import com.example.rstroybackend.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@AllArgsConstructor
@Slf4j
@Secured({ "ROLE_USER", "ROLE_ADMIN" })
@RequestMapping(value = "/api/v1/commons/user")
public class UserController {
    private UserService userService;

    @GetMapping("")
    public ResponseEntity getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findById(Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.ok(currentUser);
    }

    @PatchMapping("")
    public ResponseEntity patchCurrentUser(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        Long userId = Long.parseLong(userDetails.getUsername());
        User currentUser = userService.findById(userId);
        Map<Object, Object> errorsResponse= new HashMap<>();

        if (!currentUser.getEmail().equals(updateUserRequestDto.getEmail())) {
            try {
                userService.findByEmail(updateUserRequestDto.getEmail());
                errorsResponse.put("email", "Такая почта уже используется");
            } catch (ResourceNotFoundException e) {}
        }

        if (!currentUser.getPhoneNumber().equals(updateUserRequestDto.getPhoneNumber())) {
            try {
                userService.findByPhoneNumber(updateUserRequestDto.getPhoneNumber());
                errorsResponse.put("phoneNumber", "Такой номер уже используется");
            } catch (ResourceNotFoundException e) {}
        }

        if (errorsResponse.size() != 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorsResponse);
        }

        userService.update(updateUserRequestDto, userId);
        return ResponseEntity.ok(null);
    }

    @PatchMapping("/favorites")
    public ResponseEntity patchFavorites(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Set<ProductIdDto> products) {
        userService.updateFavorites(products, Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.ok(null);
    }

    @PatchMapping("/cart")
    public ResponseEntity patchCart(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Set<StashedProductDto> products) {
        userService.updateCart(products, Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.ok(null);
    }

    @PostMapping("/orders")
    public ResponseEntity createOrder(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CreateOrderRequestDto order) {
        Order result = userService.createOrder(order, Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.ok(result.getId());
    }

    @DeleteMapping("/orders")
    public ResponseEntity cancelOrder(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(name="id", required = true) Long id) {
        userService.cancelOrder(id, Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.ok(null);
    }

    @PatchMapping("/subscription")
    public ResponseEntity changeIsSubscribed(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(name="isSubscribed", required = true) Boolean isSubscribed) {
        userService.changeIsSubscribed(isSubscribed, Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.ok(null);
    }
}