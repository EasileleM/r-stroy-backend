package com.example.rstroybackend.service.impl;

import com.example.rstroybackend.entity.Order;
import com.example.rstroybackend.entity.Product;
import com.example.rstroybackend.entity.StashedProduct;
import com.example.rstroybackend.enums.OrderStatus;
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

import java.util.Date;

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
}
