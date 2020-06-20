package com.example.rstroybackend.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
public class CreateOrderRequestDto {
    @NotEmpty
    @NotBlank
    private Set<StashedProductDto> stashedProductDtos;

    private String description;

    @NotBlank
    private String arrivalPoint;
}
