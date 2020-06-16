package com.example.rstroybackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "types")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductType extends BaseEntity {
    @Column(name = "name")
    @NotBlank(message = "Название обязательно")
    @Size(min = 2, max = 50, message = "Название должно содержать больше двух и меньше 50ти символов")
    private String name;

    @ManyToMany(mappedBy = "types", fetch = FetchType.LAZY)
    private List<Product> products;
}
