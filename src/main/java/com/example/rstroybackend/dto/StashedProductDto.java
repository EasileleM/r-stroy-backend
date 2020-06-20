package com.example.rstroybackend.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class StashedProductDto {
    @NotBlank
    private Long productId;

    @NotBlank
    private Integer amountInStash;
}
