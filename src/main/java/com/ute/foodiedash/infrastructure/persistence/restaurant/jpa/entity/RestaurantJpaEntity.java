package com.ute.foodiedash.infrastructure.persistence.restaurant.jpa.entity;

import com.ute.foodiedash.infrastructure.persistence.common.jpa.entity.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "restaurants")
public class RestaurantJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 25, nullable = false, unique = true)
    private String code;

    @Column(length = 255, nullable = false, unique = true)
    private String slug;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(length = 255, nullable = false)
    private String address;

    @Column(length = 255, nullable = false)
    private String phone;

    @Column(precision = 10, scale = 6, nullable = false)
    private BigDecimal lat;

    @Column(precision = 10, scale = 6, nullable = false)
    private BigDecimal lng;

    @Column(length = 255, nullable = false)
    private String status;
}
