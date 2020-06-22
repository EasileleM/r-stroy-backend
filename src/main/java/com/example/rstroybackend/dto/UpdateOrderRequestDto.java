package com.example.rstroybackend.dto;

import com.example.rstroybackend.enums.OrderStatus;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Data
public class UpdateOrderRequestDto {
    @NotNull
    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;
}
