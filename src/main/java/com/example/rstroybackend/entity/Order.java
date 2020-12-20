package com.example.rstroybackend.entity;

import com.example.rstroybackend.entity.views.SecurityViews;
import com.example.rstroybackend.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "orders")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Order extends BaseEntity {
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    @JsonView(SecurityViews.User.class)
    private Set<StashedProduct> stashedProducts;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    @JsonView(SecurityViews.User.class)
    private User user;

    @DecimalMin(value = "0.0", inclusive = false, message = "Цена обязательна")
    @JsonView(SecurityViews.Anonymous.class)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @JsonView(SecurityViews.User.class)
    private OrderStatus orderStatus;

    @JsonView(SecurityViews.User.class)
    private Date startedDate;

    @JsonView(SecurityViews.User.class)
    private Date completionDate;

    @JsonView(SecurityViews.User.class)
    private String city;

    @JsonView(SecurityViews.User.class)
    private String street;

    @JsonView(SecurityViews.User.class)
    private String house;

    @JsonView(SecurityViews.User.class)
    private String description;
}
