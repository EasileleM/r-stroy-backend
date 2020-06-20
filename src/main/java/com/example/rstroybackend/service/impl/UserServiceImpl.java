package com.example.rstroybackend.service.impl;

import com.example.rstroybackend.dto.ProductIdDto;
import com.example.rstroybackend.dto.StashedProductDto;
import com.example.rstroybackend.entity.*;
import com.example.rstroybackend.repo.ProductRepo;
import com.example.rstroybackend.repo.RoleRepo;
import com.example.rstroybackend.repo.UserRepo;
import com.example.rstroybackend.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    private final ProductRepo productRepo;

    private final RoleRepo roleRepo;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public User register(User user) {
        Role roleUser = roleRepo.findByName("ROLE_USER").orElse(null);
        Set<Role> userRoles = new HashSet<>();
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
    public Set<User> getAll() {
        Set<User> result = new HashSet<>(userRepo.findAll());

        log.info("IN getAll - {} users found", result.size());
        return result;
    }

    @Override
    public User findByEmail(String email) {
        User result = userRepo.findByEmail(email).orElse(null);
        if (result == null) {
            log.info("IN findByEmail - no user found by email: {}", email);
        } else {
            log.info("IN findByEmail - user {} found by email: {}", result, email);
        }
        return result;
    }

    @Override
    public User findByPhoneNumber(String phoneNumber) {
        User result = userRepo.findByPhoneNumber(phoneNumber).orElse(null);
        if (result == null) {
            log.info("IN findByPhoneNumber - no user found by phoneNumber: {}", phoneNumber);
        } else {
            log.info("IN findByPhoneNumber - user {} found by phoneNumber: {}", result, phoneNumber);
        }
        return result;
    }

    @Override
    public User updateUserCart(Set<StashedProductDto> cartProducts, Long id) {
        User currentUser = userRepo.findById(id).orElse(null);

        Set<StashedProduct> newCartProducts = new HashSet<>();

        for (StashedProductDto stashedProductDto: cartProducts) {
            StashedProduct stashedProduct = new StashedProduct();
            Product product = productRepo.findById(stashedProductDto.getProductId()).orElse(null);
            if (product != null) {
                stashedProduct.setProduct(product);
                stashedProduct.setAmountInStash(stashedProductDto.getAmountInStash());
                stashedProduct.setCreated(new Date());
                stashedProduct.setUpdated(new Date());
                stashedProduct.setStatus(Status.ACTIVE);

                newCartProducts.add(stashedProduct);
            }
        }

        currentUser.setCartProducts(newCartProducts);

        User result = userRepo.save(currentUser);

        if (result == null) {
            log.info("IN updateUserCart - user with id: {} update with: {} failed", id, newCartProducts);
        } else {
            log.info("IN updateUserCart - user with id: {} successfully updated with: {}", id, newCartProducts);
        }
        return result;
    }

    @Override
    public User updateUserFavorites(Set<ProductIdDto> favoritesProductsIds, Long id) {
        User currentUser = userRepo.findById(id).orElse(null);

        Set<Product> newFavoritesProducts = new HashSet<>();

        for (ProductIdDto productIdDto: favoritesProductsIds) {
            Product product = productRepo.findById(productIdDto.getId()).orElse(null);
            if (product != null) {
                newFavoritesProducts.add(product);
            }
        }

        currentUser.setFavoritesProducts(newFavoritesProducts);

        User result = userRepo.save(currentUser);

        if (result == null) {
            log.info("IN updateUserFavorites - user with id: {} successfully updated with: {}", id, newFavoritesProducts);
        } else {
            log.info("IN updateUserFavorites - user with id: {} update with: {} failed", id, newFavoritesProducts);
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
