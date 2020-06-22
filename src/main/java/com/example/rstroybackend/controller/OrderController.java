package com.example.rstroybackend.controller;

import com.example.rstroybackend.dto.JsonPage;
import com.example.rstroybackend.dto.UpdateOrderRequestDto;
import com.example.rstroybackend.entity.Order;
import com.example.rstroybackend.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@Secured("ROLE_ADMIN")
@RequestMapping(value = "/api/v1/admin/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("")
    public ResponseEntity getAllOrders(@PageableDefault(size = 30, sort = { "orderStatus", "startedDate", "completionDate" }, direction = Sort.Direction.DESC) Pageable pageable) {
        JsonPage<Order> orders = new JsonPage(orderService.findAll(pageable), pageable);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("{id}")
    public ResponseEntity getOrder(@PathVariable(name = "id", required = true) Long id) {
        Order order = orderService.findById(id);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("{id}")
    public ResponseEntity updateOrderStatus(@PathVariable(name = "id", required = true) Long id, @Valid @RequestBody UpdateOrderRequestDto updateOrderRequestDto) {
        Order order = orderService.updateOrderStatus(updateOrderRequestDto.getOrderStatus(), id);
        return ResponseEntity.ok(order);
    }
}