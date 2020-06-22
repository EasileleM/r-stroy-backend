package com.example.rstroybackend.controller;

import com.example.rstroybackend.dto.*;
import com.example.rstroybackend.entity.Order;
import com.example.rstroybackend.entity.User;
import com.example.rstroybackend.exceptions.ResourceNotFoundException;
import com.example.rstroybackend.service.OrderService;
import com.example.rstroybackend.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
@RequestMapping(value = "/api/v1")
public class UserController {
    private UserService userService;

    private OrderService orderService;

    @GetMapping("/commons/user")
    public ResponseEntity getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findById(Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.ok(currentUser);
    }

    @PatchMapping("/commons/user")
    public ResponseEntity patchCurrentUser(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UpdateCurrentUserRequestDto updateCurrentUserRequestDto) {
        Long userId = Long.parseLong(userDetails.getUsername());
        User currentUser = userService.findById(userId);
        Map<Object, Object> errorsResponse= new HashMap<>();

        if (!currentUser.getEmail().equals(updateCurrentUserRequestDto.getEmail())) {
            try {
                userService.findByEmail(updateCurrentUserRequestDto.getEmail());
                errorsResponse.put("email", "Такая почта уже используется");
            } catch (ResourceNotFoundException e) {}
        }

        if (!currentUser.getPhoneNumber().equals(updateCurrentUserRequestDto.getPhoneNumber())) {
            try {
                userService.findByPhoneNumber(updateCurrentUserRequestDto.getPhoneNumber());
                errorsResponse.put("phoneNumber", "Такой номер уже используется");
            } catch (ResourceNotFoundException e) {}
        }

        if (errorsResponse.size() != 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorsResponse);
        }

        userService.update(updateCurrentUserRequestDto, userId);
        return ResponseEntity.ok(null);
    }

    @PatchMapping("/commons/user/favorites")
    public ResponseEntity patchFavorites(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Set<ProductIdDto> products) {
        userService.updateFavorites(products, Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.ok(null);
    }

    @PatchMapping("/commons/user/cart")
    public ResponseEntity patchCart(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Set<StashedProductDto> products) {
        userService.updateCart(products, Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.ok(null);
    }

    @PostMapping("/commons/user/orders")
    public ResponseEntity createOrder(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CreateOrderRequestDto order) {
        Order result = userService.createOrder(order, Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.ok(result.getId());
    }

    @DeleteMapping("/commons/user/orders")
    public ResponseEntity cancelOrder(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(name="id", required = true) Long id) {
        userService.cancelOrder(id, Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.ok(null);
    }

    @PatchMapping("/commons/user/subscription")
    public ResponseEntity changeIsSubscribed(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(name="isSubscribed", required = true) Boolean isSubscribed) {
        userService.changeIsSubscribed(isSubscribed, Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.ok(null);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/admin/user/{id}")
    public ResponseEntity getUserById(@PathVariable(name = "id", required = true) Long id) {
        User targetUser = userService.findById(id);
        return ResponseEntity.ok(targetUser);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/admin/user/all")
    public ResponseEntity getAllUsers(@PageableDefault(size = 30, sort = { "created" }, direction = Sort.Direction.DESC) Pageable pageable) {
        JsonPage<User>  users = new JsonPage(userService.findAll(pageable), pageable);
        return ResponseEntity.ok(users);
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping("/admin/user/{id}")
    public ResponseEntity updateUser(@PathVariable(name = "id", required = true) Long id, @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        User result = userService.update(updateUserRequestDto, id);
        return ResponseEntity.ok(result);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/admin/user/{id}")
    public ResponseEntity deleteUser(@PathVariable(name = "id", required = true) Long id) {
        userService.delete(id);
        return ResponseEntity.ok(null);
    }
}