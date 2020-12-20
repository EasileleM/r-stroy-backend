package com.example.rstroybackend.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
public class CreateOrderRequestDto {
    @NotEmpty
    private Set<StashedProductDto> stashedProductDtos;

    private String description;

    @NotBlank
    private String city;

    @NotBlank
    private String street;

    @NotBlank
    private String house;
}
