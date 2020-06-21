package com.example.rstroybackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "stashedProducts")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class StashedProduct extends BaseEntity {
    @OneToOne // TODO it is rather Many to One
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    private Integer amountInStash;
}
