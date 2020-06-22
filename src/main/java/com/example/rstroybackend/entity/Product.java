package com.example.rstroybackend.entity;

import com.example.rstroybackend.entity.views.SecurityViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
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
    @JsonView(SecurityViews.Anonymous.class)
    private String name;

    @NotBlank(message = "Описание обязательно")
    @Size(min = 10, max = 1050, message = "Описание должно содержать больше десяти и меньше 1050ти символов")
    @JsonView(SecurityViews.Anonymous.class)
    private String description;

    @PositiveOrZero(message = "Количество обязательно")
    @JsonView(SecurityViews.Anonymous.class)
    private Integer amount;

    @DecimalMin(value = "0.0", inclusive = false, message = "Цена обязательна")
    @JsonView(SecurityViews.Anonymous.class)
    private BigDecimal price;

    @NotBlank(message = "Картинка обязательна")
    @JsonView(SecurityViews.Anonymous.class)
    private String imageURL;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "product_types",
            joinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "type_id", referencedColumnName = "id")}
    )
    @JsonView(SecurityViews.Anonymous.class)
    private Set<ProductType> types;

    @ManyToMany(mappedBy = "favoritesProducts", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<User> users;

    @OneToMany(mappedBy="product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<StashedProduct> stashedProducts;
}
