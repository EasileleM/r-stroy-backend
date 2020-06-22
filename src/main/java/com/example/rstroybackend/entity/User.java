package com.example.rstroybackend.entity;

import com.example.rstroybackend.entity.views.SecurityViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
@SuperBuilder
public class User extends BaseEntity {
    @NotBlank(message = "Имя обязательно к заполнению")
    @Size(min = 2, max = 50, message = "Имя должно содержать больше двух и меньше 50ти символов")
    @JsonView(SecurityViews.User.class)
    private String firstName;

    @NotBlank(message = "Фамилия обязательна к заполнению")
    @Size(min = 2, max = 50, message = "Фамилия должна содержать больше двух и меньше 50ти символов")
    @JsonView(SecurityViews.User.class)
    private String lastName;

    @Email(message = "Некорректный email")
    @NotBlank(message = "Email обязателен")
    @JsonView(SecurityViews.User.class)
    private String email;

    @NotBlank(message = "Номер телефона обязателен")
    @Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$", message = "Некорректный номер телефона")
    @JsonView(SecurityViews.User.class)
    private String phoneNumber;

    @NotBlank(message = "Пароль обязателен")
    @JsonIgnore
    private String password;

    @JsonView(SecurityViews.User.class)
    private Boolean isSubscribed;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonView(SecurityViews.Admin.class)
    @JoinTable(
            name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private Set<Role> roles;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    @JsonView(SecurityViews.User.class)
    private Set<StashedProduct> cartProducts;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_favorites",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")}
    )
    @JsonView(SecurityViews.User.class)
    private Set<Product> favoritesProducts;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @JsonView(SecurityViews.User.class)
    private Set<Order> orders;

    public void addOrder(Order order) {
        orders.add(order);
        order.setUser(this);
    }

    public void removeOrder(Order order) {
        orders.remove(order);
        order.setUser(null);
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
