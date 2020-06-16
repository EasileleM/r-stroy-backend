package com.example.rstroybackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {
    @Column(name = "first_name")
    @NotBlank(message = "Имя обязательно к заполнению")
    @Size(min = 2, max = 50, message = "Имя должно содержать больше двух и меньше 50ти символов")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "Фамилия обязательна к заполнению")
    @Size(min = 2, max = 50, message = "Фамилия должна содержать больше двух и меньше 50ти символов")
    private String lastName;

    @Column(name = "email")
    @Email(message = "Некорректный email")
    @NotBlank(message = "Email обязателен")
    private String email;

    @Column(name = "phoneNumber")
    @NotBlank(message = "Номер телефона обязателен")
    @Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$", message = "Некорректный номер телефона")
    private String phoneNumber;

    @Column(name = "password")
    @NotBlank(message = "Пароль обязателен")
    @JsonIgnore
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private List<Role> roles;
}
