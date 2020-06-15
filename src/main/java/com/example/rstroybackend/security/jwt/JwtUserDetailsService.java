package com.example.rstroybackend.security.jwt;

import com.example.rstroybackend.entity.User;
import com.example.rstroybackend.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User with username: " + email + " not found");
        }

        JwtUser jwtUser = JwtUserFactory.create(user);

        log.info("IN loadUserByUsername - user with username: {} successfully loaded", email);

        return jwtUser;
    }
}