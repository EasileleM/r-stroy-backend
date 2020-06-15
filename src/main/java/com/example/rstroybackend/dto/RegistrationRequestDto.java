package com.example.rstroybackend.dto;

import com.example.rstroybackend.entity.User;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class RegistrationRequestDto {
    @NotBlank(message = "Имя обязательно к заполнению")
    @Size(min = 2, max = 50, message = "Имя должно содержать больше двух и меньше 50ти символов")
    private String firstName;

    @NotBlank(message = "Фамилия обязательна к заполнению")
    @Size(min = 2, max = 50, message = "Фамилия должна содержать больше двух и меньше 50ти символов")
    private String lastName;

    @Email(message = "Некорректный email")
    @NotBlank(message = "Email обязателен")
    private String email;

    @NotBlank(message = "Номер телефона обязателен")
    @Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$", message = "Некорректный номер телефона")
    private String phoneNumber;

    @NotBlank(message = "Пароль обязателен")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "Пароль должен содержать минимум 8 латинских букв, в том числе одну цифру")
    private String password;

    public User toUser() {
        return new User(
                firstName,
                lastName,
                email,
                phoneNumber,
                password,
                null
        );
    }
}
