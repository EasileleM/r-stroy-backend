package com.example.rstroybackend.controller;

import com.example.rstroybackend.dto.ProductIdDto;
import com.example.rstroybackend.dto.StashedProductDto;
import com.example.rstroybackend.entity.User;
import com.example.rstroybackend.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@AllArgsConstructor
@Slf4j
@Secured({ "ROLE_USER", "ROLE_ADMIN" })
@RequestMapping(value = "/api/v1/commons/user")
public class UserController {
    private UserService userService;

    @GetMapping("")
    public ResponseEntity currentUser(@AuthenticationPrincipal UserDetails userDetails) {
        log.warn(userDetails.getUsername());
        User currentUser = userService.findById(Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.ok(currentUser);
    }

    @PatchMapping("/favorites")
    public ResponseEntity patchFavorites(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Set<ProductIdDto> products) {
        userService.updateUserFavorites(products, Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.ok(null);
    }

    @PatchMapping("/cart")
    public ResponseEntity patchCart(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Set<StashedProductDto> products) {
        userService.updateUserCart(products, Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.ok(null);
    }
}