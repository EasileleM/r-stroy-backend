package com.example.rstroybackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "products")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseEntity {
    @NotBlank(message = "Название обязательно")
    @Size(min = 2, max = 50, message = "Название должно содержать больше двух и меньше 50ти символов")
    private String name;

    @NotBlank(message = "Описание обязательно")
    @Size(min = 10, max = 1050, message = "Описание должно содержать больше десяти и меньше 1050ти символов")
    private String description;

    @PositiveOrZero(message = "Количество обязательно")
    private Integer amount;

    @DecimalMin(value = "0.0", inclusive = false, message = "Цена обязательна")
    private BigDecimal price;

    @NotBlank(message = "Картинка обязательна")
    private String imageURL;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "product_types",
            joinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "type_id", referencedColumnName = "id")}
    )
    private Set<ProductType> types;

    @ManyToMany(mappedBy = "favoritesProducts", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<User> users;
}
