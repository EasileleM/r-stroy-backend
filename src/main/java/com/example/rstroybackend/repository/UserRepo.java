package com.example.rstroybackend.repository;

import com.example.rstroybackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByPhoneNumber(String phoneNumber);
}
