package com.example.rstroybackend.service;

import com.example.rstroybackend.entity.User;

import java.util.List;

public interface UserService {
    User register(User user);

    List<User> getAll();

    User findByEmail(String email);

    User findByPhoneNumber(String phoneNumber);

    User findById(Long id);

    void delete(Long id);
}
