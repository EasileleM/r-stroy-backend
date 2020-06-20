package com.example.rstroybackend.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ProductIdDto {
    @NotBlank
    private Long id;
}
