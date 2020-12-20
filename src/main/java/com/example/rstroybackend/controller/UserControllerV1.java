package com.example.rstroybackend.controller;

import com.example.rstroybackend.dto.*;
import com.example.rstroybackend.entity.Order;
import com.example.rstroybackend.entity.User;
import com.example.rstroybackend.exceptions.BadRequestException;
import com.example.rstroybackend.exceptions.ConflictException;
import com.example.rstroybackend.service.MailService;
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

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.Set;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(value = "/api/v1")
public class UserControllerV1 {
    private UserService userService;

    private OrderService orderService;

    private MailService mailService;

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @GetMapping("/commons/user")
    public ResponseEntity getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findById(Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.ok(currentUser);
    }

    @PostMapping("/commons/user/activate/{activationCode}")
    public ResponseEntity activateUser(@PathVariable String activationCode) {
        userService.activate(activationCode);
        return ResponseEntity.ok(null);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @PatchMapping("/commons/user")
    public ResponseEntity patchCurrentUser(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody UpdateCurrentUserRequestDto updateCurrentUserRequestDto) {
        try {
            Long userId = Long.parseLong(userDetails.getUsername());
            User result = userService.update(updateCurrentUserRequestDto, userId);
            return ResponseEntity.ok(result);
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getBody());
        }
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @PatchMapping("/commons/user/favorites")
    public ResponseEntity patchFavorites(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody Set<ProductIdDto> products) {
        userService.updateFavorites(products, Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.ok(null);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @PatchMapping("/commons/user/cart")
    public ResponseEntity patchCart(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody Set<StashedProductDto> products) {
        userService.updateCart(products, Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.ok(null);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @PostMapping("/commons/user/orders")
    public ResponseEntity createOrder(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody CreateOrderRequestDto order) {
        try {
            orderService.validateCity(order.getCity());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getBody());
        } catch (Exception e) {
           throw e;
        }

        Order result = userService.createOrder(order, Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.ok(result.getId());
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @DeleteMapping("/commons/user/orders")
    public ResponseEntity cancelOrder(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(name="id", required = true) Long id) {
        userService.cancelOrder(id, Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.ok(null);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
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
    public ResponseEntity updateUser(@PathVariable(name = "id", required = true) Long id, @Valid @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        try {
            User updatedUser = userService.update(updateUserRequestDto, id);
            return ResponseEntity.ok(updatedUser);
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getBody());
        }
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/admin/user/{id}")
    public ResponseEntity deleteUser(@PathVariable(name = "id", required = true) Long id) {
        userService.delete(id);
        return ResponseEntity.ok(null);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/admin/user/subscribers/notify")
    public ResponseEntity notifySubscribers(@Valid @RequestBody MailMessageDto mailMessageDto) throws MessagingException {
        mailService.sendToAllSubscribers(mailMessageDto);
        return ResponseEntity.ok(null);
    }
}