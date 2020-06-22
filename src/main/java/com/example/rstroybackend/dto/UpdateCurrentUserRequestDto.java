package com.example.rstroybackend.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UpdateCurrentUserRequestDto extends UpdateUserRequestDto {
    @NotBlank(message = "Пароль обязателен")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "Пароль должен содержать минимум 8 латинских букв, в том числе одну цифру")
    private String password;
}
