package com.example.rstroybackend.entity;

import com.example.rstroybackend.entity.views.SecurityViews;
import com.fasterxml.jackson.annotation.JsonView;
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
    @JsonView(SecurityViews.User.class)
    private Product product;

    @JsonView(SecurityViews.User.class)
    private Integer amountInStash;
}
