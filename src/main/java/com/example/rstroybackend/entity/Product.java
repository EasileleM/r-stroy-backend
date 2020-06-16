package com.example.rstroybackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseEntity {
    @Column(name = "name")
    @NotBlank(message = "Название обязательно")
    @Size(min = 2, max = 50, message = "Название должно содержать больше двух и меньше 50ти символов")
    private String name;

    @Column(name = "description")
    @NotBlank(message = "Описание обязательно")
    @Size(min = 10, max = 1050, message = "Описание должно содержать больше десяти и меньше 1050ти символов")
    private String description;

    @Column(name = "amount")
    @Positive(message = "Количество обязательно")
    private Integer amount;

    @Column(name = "price")
    @DecimalMin(value = "0.0", inclusive = false, message = "Цена обязательна")
    private BigDecimal price;

    @Column(name = "imageURL")
    @NotBlank(message = "Картинка обязательна")
    private String imageURL;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "product_types",
            joinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "type_id", referencedColumnName = "id")}
    )
    private List<ProductType> types;
}
