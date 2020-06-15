package com.example.rstroybackend.service.impl;

import com.example.rstroybackend.entity.Role;
import com.example.rstroybackend.entity.Status;
import com.example.rstroybackend.entity.User;
import com.example.rstroybackend.repository.RoleRepo;
import com.example.rstroybackend.repository.UserRepo;
import com.example.rstroybackend.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    private final RoleRepo roleRepo;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public User register(User user) {
        Role roleUser = roleRepo.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);
        user.setStatus(Status.ACTIVE);
        user.setCreated(new Date());
        user.setUpdated(new Date());

        User registeredUser = userRepo.save(user);

        log.info("IN register - user: {} successfully registered", registeredUser);

        return registeredUser;
    }

    @Override
    public List<User> getAll() {
        List<User> result = userRepo.findAll();

        log.info("IN getAll - {} users found", result.size());
        return result;
    }

    @Override
    public User findByEmail(String email) {
        User result = userRepo.findByEmail(email);
        if (result == null) {
            log.info("IN findByEmail - no user found by email: {}", email);
        } else {
            log.info("IN findByEmail - user {} found by email: {}", result, email);
        }
        return result;
    }

    @Override
    public User findByPhoneNumber(String phoneNumber) {
        User result = userRepo.findByPhoneNumber(phoneNumber);
        if (result == null) {
            log.info("IN findByPhoneNumber - no user found by phoneNumber: {}", phoneNumber);
        } else {
            log.info("IN findByPhoneNumber - user {} found by phoneNumber: {}", result, phoneNumber);
        }
        return result;
    }

    @Override
    public User findById(Long id) {
        User result = userRepo.findById(id).orElse(null);

        if (result == null) {
            log.warn("IN findById - no user found by id: {}", id);
        } else {
            log.info("IN findById - user {} found by id: {}", result, id);
        }

        return result;
    }

    @Override
    public void delete(Long id) {
        userRepo.deleteById(id);

        log.info("IN delete - user with id: {} successfully deleted", id);
    }
}
