package com.example.rstroybackend.entity;

import com.example.rstroybackend.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order extends BaseEntity {
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StashedProduct> stashedProducts;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private User user;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private Date completionDate;

    private String arrivalPoint;

    private String description;
}
