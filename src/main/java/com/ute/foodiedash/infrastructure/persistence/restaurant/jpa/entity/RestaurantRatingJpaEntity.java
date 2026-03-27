package com.ute.foodiedash.infrastructure.persistence.restaurant.jpa.entity;

import com.ute.foodiedash.infrastructure.persistence.common.jpa.entity.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "restaurant_ratings")
public class RestaurantRatingJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "rating_avg",
            precision = 3,
            scale = 2,
            nullable = false
    )
    private BigDecimal ratingAvg;

    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;

    @Column(
            name = "rating_count",
            nullable = false
    )
    private Integer ratingCount;
}
