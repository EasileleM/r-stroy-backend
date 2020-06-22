package com.example.rstroybackend.entity;

import com.example.rstroybackend.entity.views.SecurityViews;
import com.example.rstroybackend.enums.Status;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {
    @Id
    @JsonView(SecurityViews.Anonymous.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate // TODO make it work
    @JsonView(SecurityViews.Admin.class)
    @Column(name = "created")
    private Date created;

    @LastModifiedDate
    @JsonView(SecurityViews.Admin.class)
    @Column(name = "updated")
    private Date updated;

    @Enumerated(EnumType.STRING)
    @JsonView(SecurityViews.Admin.class)
    @Column(name="status")
    private Status status;
}
