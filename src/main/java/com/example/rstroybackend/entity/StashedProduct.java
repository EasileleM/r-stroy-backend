package com.example.rstroybackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "stashedProducts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StashedProduct extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    private Integer amountInStash;
}
