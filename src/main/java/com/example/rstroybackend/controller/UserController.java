package com.example.rstroybackend.controller;

import com.example.rstroybackend.entity.User;
import com.example.rstroybackend.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
@Secured({ "ROLE_USER", "ROLE_ADMIN" })
@RequestMapping(value = "/api/v1/commons/user")
public class UserController {
    private UserService userService;

    @GetMapping("")
    public ResponseEntity currentUser(@AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(currentUser);
    }
}