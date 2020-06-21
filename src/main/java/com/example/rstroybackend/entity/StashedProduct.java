package com.example.rstroybackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "stashedProducts")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class StashedProduct extends BaseEntity {
    @ManyToOne
    @JoinColumn(name="product_id", nullable=false)
    private Product product;

    private Integer amountInStash;
}
