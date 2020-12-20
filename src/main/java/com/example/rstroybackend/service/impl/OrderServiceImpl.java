package com.example.rstroybackend.service.impl;

import com.example.rstroybackend.entity.Order;
import com.example.rstroybackend.entity.Product;
import com.example.rstroybackend.entity.StashedProduct;
import com.example.rstroybackend.entity.User;
import com.example.rstroybackend.enums.OrderStatus;
import com.example.rstroybackend.exceptions.BadRequestException;
import com.example.rstroybackend.exceptions.InternalServerErrorException;
import com.example.rstroybackend.exceptions.ResourceNotFoundException;
import com.example.rstroybackend.repo.OrderRepo;
import com.example.rstroybackend.repo.ProductRepo;
import com.example.rstroybackend.service.OrderService;
import com.example.rstroybackend.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private OrderRepo orderRepo;

    private ProductService productService;

    private ProductRepo productRepo;

    @Override
    public Page<Order> findAll(Pageable pageable) {
        Page<Order> result = orderRepo.findAll(pageable);

        return result;
    }

    @Override
    public List<Order> findAll() {
        List<Order> result = orderRepo.findAll();

        return result;
    }

    @Override
    public Order findById(Long id) {
        Order result = orderRepo.findById(id).orElse(null);

        if (result == null) {
            log.warn("IN findById - no order found by id: {}", id);
            throw new ResourceNotFoundException();
        }
        log.info("IN findById - order {} found by id: {}", result, id);

        return result;
    }

    @Override
    @Transactional
    public Order updateOrderStatus(OrderStatus status, Long orderId) {
        Order targetOrder = orderRepo.findById(orderId).orElse(null);

        if (targetOrder == null) {
            log.warn("IN updateOrderStatus - no order found by id: {}", orderId);
            throw new ResourceNotFoundException();
        }
        log.info("IN updateOrderStatus - order {} found by id: {}", targetOrder, orderId);

        if (status == OrderStatus.CANCELED) {
            for (StashedProduct stashedProduct: targetOrder.getStashedProducts()) {
                Product product = productService.findById(stashedProduct.getProduct().getId());

                product.setAmount(stashedProduct.getAmountInStash() + product.getAmount());
                product.setUpdated(new Date());

                productRepo.save(product);
            }
        } else if (status == OrderStatus.COMPLETED) {
            targetOrder.setCompletionDate(new Date());
        }

        targetOrder.setOrderStatus(status);
        Order result = orderRepo.save(targetOrder);

        if (result == null) {
            log.info("IN updateOrderStatus - order with id: {} update failed", orderId);
            throw new InternalServerErrorException();
        }

        log.info("IN updateOrderStatus - order with id: {} successfully updated", orderId);

        return result;
    }

    @Override
    public User findUserByOrder(Long orderId) {
        Order targetOrder = orderRepo.findById(orderId).orElse(null);

        if (targetOrder == null) {
            log.warn("IN findUserByOrder - no order found by id: {}", orderId);
            throw new ResourceNotFoundException();
        }

        User targetUser = targetOrder.getUser();

        if (targetUser == null) {
            log.warn("IN findUserByOrder - no user found");
            throw new ResourceNotFoundException();
        }

        log.info("IN findUserByOrder - user found: {}", targetUser);

        return targetUser;
    }

    @Override
    public void validateCity(String city) {
        String cities;

        try {
            URL url = new URL("https://api.hh.ru/areas");
            URLConnection connection = url.openConnection();
            InputStream is = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            cities = response.toString();

        } catch (Exception e) {
            throw new InternalServerErrorException();
        }

        if (!cities.toLowerCase().contains("\"" + city.toLowerCase() + "\"")) {
            Map<Object, Object> errorsResponse= new HashMap<>();
            errorsResponse.put("city", "Такого города не существует");
            throw new BadRequestException(errorsResponse);
        }
    }
}
