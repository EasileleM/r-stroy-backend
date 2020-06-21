package com.example.rstroybackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity {
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
    @JsonIgnore
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    @JsonManagedReference
    @JoinTable(
            name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private Set<Role> roles;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cart_user_id")
    private Set<StashedProduct> cartProducts;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_favorites",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")}
    )
    private Set<Product> favoritesProducts;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Order> orders;

    public void addOrder(Order order) {
        order.setUser(this);
        orders.add(order);
    }

    public void removeOrder(Order order) {
        order.setUser(null);
        orders.remove(order);
    }

    public void setCartProducts(Set<StashedProduct> cartProducts) {
        if (this.cartProducts == null) {
            this.cartProducts = cartProducts;
        } else {
            this.cartProducts.retainAll(cartProducts);
            this.cartProducts.addAll(cartProducts);
        }
    }
}
