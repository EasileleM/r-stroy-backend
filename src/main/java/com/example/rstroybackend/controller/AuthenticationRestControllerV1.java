package com.example.rstroybackend.controller;

import com.example.rstroybackend.dto.AuthenticationRequestDto;
import com.example.rstroybackend.dto.RegistrationRequestDto;
import com.example.rstroybackend.entity.User;
import com.example.rstroybackend.security.jwt.JwtTokenProvider;
import com.example.rstroybackend.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(value = "/api/v1/commons/auth")
public class AuthenticationRestControllerV1 {
    private AuthenticationManager authenticationManager;

    private JwtTokenProvider jwtTokenProvider;

    private UserService userService;

    @PostMapping(value = "/login")
    public ResponseEntity login(@Valid @RequestBody AuthenticationRequestDto requestDto, HttpServletResponse response) {
        try {
            String email = requestDto.getEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, requestDto.getPassword()));
            User user = userService.findByEmail(email);

            if (user == null) {
                throw new UsernameNotFoundException("User with email: " + email + " not found");
            }

            String token = jwtTokenProvider.createToken(email, user.getRoles());

            Cookie cookie = new Cookie("JWTBEARERTOKEN", "Bearer_" + token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");

            response.addCookie(cookie);

            return ResponseEntity.ok(null);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    @PostMapping(value = "/logout")
    public ResponseEntity logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("JWTBEARERTOKEN", null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);

        return ResponseEntity.ok(null);
    }

    @PostMapping("/registration")
    public ResponseEntity registration(@Valid @RequestBody RegistrationRequestDto requestDto) {
        String email = requestDto.getEmail();
        String phoneNumber = requestDto.getPhoneNumber();
        Map<Object, Object> errorsResponse= new HashMap<>();

        User userWithEmail = userService.findByEmail(email);

        if (userWithEmail != null) {
            errorsResponse.put("email", "Такая почта уже используется");
        }

        User userWithPhoneNumber = userService.findByPhoneNumber(phoneNumber);

        if (userWithPhoneNumber != null) {
            errorsResponse.put("phoneNumber", "Такой номер уже используется");
        }

        if (errorsResponse.size() != 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorsResponse);
        }

        userService.register(requestDto.toUser());

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
