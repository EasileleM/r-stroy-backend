package com.example.rstroybackend.service.impl;

import com.example.rstroybackend.dto.*;
import com.example.rstroybackend.entity.*;
import com.example.rstroybackend.enums.OrderStatus;
import com.example.rstroybackend.enums.Status;
import com.example.rstroybackend.exceptions.BadRequestException;
import com.example.rstroybackend.exceptions.InternalServerErrorException;
import com.example.rstroybackend.exceptions.ResourceNotFoundException;
import com.example.rstroybackend.exceptions.ServiceUnavailableException;
import com.example.rstroybackend.repo.*;
import com.example.rstroybackend.service.ProductService;
import com.example.rstroybackend.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    private final ProductRepo productRepo;

    private final StashedProductRepo stashedProductRepo;

    private final OrderRepo orderRepo;

    private final RoleRepo roleRepo;

    private final ProductService productService;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Set<User> findAll() {
        Set<User> users = new HashSet<>(userRepo.findAll());

        log.info("IN getAll - {} users found", users.size());
        return users;
    }

    @Override
    public User findById(Long userId) {
        User result = userRepo.findById(userId).orElse(null);

        if (result == null) {
            log.warn("IN findById - no user found by id: {}", userId);
            throw new ResourceNotFoundException();
        }

        log.info("IN findById - user {} found by id: {}", result, userId);
        return result;
    }

    @Override
    public User findByEmail(String email) {
        User user = userRepo.findByEmail(email).orElse(null);

        if (user == null) {
            log.info("IN findByEmail - no user found by email: {}", email);
            throw new ResourceNotFoundException();
        }

        log.info("IN findByEmail - user {} found by email: {}", user, email);
        return user;
    }

    @Override
    public User findByPhoneNumber(String phoneNumber) {
        User user = userRepo.findByPhoneNumber(phoneNumber).orElse(null);

        if (user == null) {
            log.info("IN findByPhoneNumber - no user found by phoneNumber: {}", phoneNumber);
            throw new ResourceNotFoundException();
        }

        log.info("IN findByPhoneNumber - user {} found by phoneNumber: {}", user, phoneNumber);
        return user;
    }

    @Override
    public User register(RegistrationRequestDto userDto) {
        Role roleUser = roleRepo.findByName("ROLE_USER").orElse(null);
        if (roleUser == null) {
            throw new ServiceUnavailableException();
        }

        User newUser = userDto.toUser();

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(roleUser);

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRoles(userRoles);
        newUser.setStatus(Status.ACTIVE);
        newUser.setCreated(new Date());
        newUser.setUpdated(new Date());
        newUser.setIsSubscribed(userDto.getIsSubscribed());

        User registeredUser = userRepo.save(newUser);

        if (registeredUser == null) {
            log.info("IN register - user: {} registration failed", newUser);
            throw new InternalServerErrorException();
        }

        log.info("IN register - user: {} successfully registered", registeredUser);

        return registeredUser;
    }

    @Override
    public User update(UpdateUserRequestDto updateUserRequestDto, Long userId) {
        User currentUser = findById(userId);

        if (!passwordEncoder.matches(updateUserRequestDto.getPassword(), currentUser.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        currentUser.setEmail(updateUserRequestDto.getEmail());
        currentUser.setPhoneNumber(updateUserRequestDto.getPhoneNumber());
        currentUser.setFirstName(updateUserRequestDto.getFirstName());
        currentUser.setLastName(updateUserRequestDto.getLastName());
        currentUser.setUpdated(new Date());

        Boolean subscriptionStatus = updateUserRequestDto.getIsSubscribed();
        if (subscriptionStatus != null) {
            currentUser.setIsSubscribed(subscriptionStatus);
        }

        String newPassword = updateUserRequestDto.getNewPassword();
        if (newPassword != null && newPassword.length() != 0) {
            currentUser.setPassword(passwordEncoder.encode(newPassword));
        }

        User result = userRepo.save(currentUser);

        if (result == null) {
            log.info("IN update - user with id: {} update: {} failed", userId, result);
            throw new InternalServerErrorException();
        }
        log.info("IN update - user with id: {} successfully updated: {}", userId, result);

        return result;
    }

    @Override
    public void delete(Long id) {
        userRepo.deleteById(id);

        log.info("IN delete - user with id: {} successfully deleted", id);
    }

    @Override
    public User updateCart(Set<StashedProductDto> cartProducts, Long userId) {
        User currentUser = findById(userId);

        Set<StashedProduct> newCartProducts = new HashSet<>();

        for (StashedProductDto stashedProductDto: cartProducts) {
            Product product = productService.findById(stashedProductDto.getProductId());

            if (product.getAmount() == 0 || product.getAmount() < stashedProductDto.getAmountInStash()) {
                throw new BadRequestException();
            }

            StashedProduct stashedProduct = StashedProduct.builder()
                    .product(product)
                    .amountInStash(stashedProductDto.getAmountInStash())
                    .created(new Date())
                    .updated(new Date())
                    .status(Status.ACTIVE)
                    .build();
            newCartProducts.add(stashedProduct);
        }

        currentUser.setCartProducts(newCartProducts);

        User result = userRepo.save(currentUser);

        if (result == null) {
            log.info("IN updateUserCart - user with id: {} update with: {} failed", userId, newCartProducts);
            throw new InternalServerErrorException();
        }

        log.info("IN updateUserCart - user with id: {} successfully updated with: {}", userId, newCartProducts);
        return result;
    }

    @Override
    public User updateFavorites(Set<ProductIdDto> favoritesProductsIds, Long userId) {
        User currentUser = findById(userId);

        Set<Product> newFavoritesProducts = new HashSet<>();

        for (ProductIdDto productIdDto: favoritesProductsIds) {
            Product product = productService.findById(productIdDto.getId());

            newFavoritesProducts.add(product);
        }

        currentUser.setFavoritesProducts(newFavoritesProducts);

        User result = userRepo.save(currentUser);

        if (result == null) {
            log.info("IN updateUserFavorites - user with id: {} update with: {} failed", userId, newFavoritesProducts);
            throw new InternalServerErrorException();
        }

        log.info("IN updateUserFavorites - user with id: {} successfully updated with: {}", userId, newFavoritesProducts);
        return result;
    }

    @Override
    @Transactional
    public Order createOrder(CreateOrderRequestDto order, Long userId) {
        User currentUser = findById(userId);

        Order newOrder = Order.builder()
                .created(new Date())
                .updated(new Date())
                .status(Status.ACTIVE)
                .orderStatus(OrderStatus.REGISTRATION)
                .description(order.getDescription())
                .arrivalPoint(order.getArrivalPoint())
                .build();

        Set<StashedProduct> orderProducts = new HashSet<>();

        for (StashedProductDto stashedProductDto: order.getStashedProductDtos()) {
            Product product = productService.findById(stashedProductDto.getProductId());

            if (product.getAmount() == 0 || product.getAmount() < stashedProductDto.getAmountInStash()) {
                throw new BadRequestException();
            }

            StashedProduct stashedProduct = StashedProduct.builder()
                    .product(product)
                    .amountInStash(stashedProductDto.getAmountInStash())
                    .created(new Date())
                    .updated(new Date())
                    .status(Status.ACTIVE)
                    .build();

            Integer newProductAmount = product.getAmount() - stashedProductDto.getAmountInStash();

            if (newProductAmount == 0) {
                stashedProductRepo.deleteCartItemsByProductId(product.getId());
            }

            product.setAmount(newProductAmount);
            product.setUpdated(new Date());

            productRepo.save(product);

            orderProducts.add(stashedProduct);
        }

        newOrder.setStashedProducts(orderProducts);

        currentUser.addOrder(newOrder);

        User result = userRepo.save(currentUser);

        if (result == null) {
            log.info("IN createOrder - user with id: {} order creation: {} failed", userId, newOrder);
            throw new InternalServerErrorException();
        }

        Order createdOrder = result
                .getOrders()
                .stream()
                .sorted((order1, order2) -> order2.getCreated().compareTo(order1.getCreated()))
                .findFirst()
                .get();

        log.info("IN createOrder - user with id: {} successfully created order: {}", userId, createdOrder);
        return createdOrder;
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId, Long userId) {
        User currentUser = findById(userId);

        Order canceledOrder = orderRepo.findById(orderId).orElse(null);

        if (canceledOrder == null
                || canceledOrder.getUser().getId() != userId
                || canceledOrder.getOrderStatus() != OrderStatus.REGISTRATION) {
            throw new BadRequestException();
        }

        for (StashedProduct stashedProduct: canceledOrder.getStashedProducts()) {
            Product product = productService.findById(stashedProduct.getProduct().getId());

            product.setAmount(stashedProduct.getAmountInStash() + product.getAmount());
            product.setUpdated(new Date());

            productRepo.save(product);
        }

        currentUser.removeOrder(canceledOrder);

        User result = userRepo.save(currentUser);

        if (result == null) {
            log.info("IN cancelOrder - user with id: {} order cancellation: {} failed", userId, canceledOrder);
            throw new InternalServerErrorException();
        }

        log.info("IN cancelOrder - user with id: {} successfully canceled order: {}", userId, canceledOrder);
    }

    @Override
    public void changeIsSubscribed(Boolean isSubscribed, Long userId) {
        User currentUser = findById(userId);

        currentUser.setIsSubscribed(isSubscribed);

        User result = userRepo.save(currentUser);

        if (result == null) {
            log.info("IN update - user with id: {} update: {} failed", userId, result);
            throw new InternalServerErrorException();
        }
        log.info("IN update - user with id: {} successfully updated: {}", userId, result);
    }
}
