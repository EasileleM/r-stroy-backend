package com.example.rstroybackend.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
public class StashedProductDto {
    @NotBlank
    private Long productId;

    @NotBlank
    @Positive
    private Integer amountInStash;
}
