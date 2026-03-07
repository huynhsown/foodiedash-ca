package com.ute.foodiedash.infrastructure.persistence.restaurant.jpa.entity;

import com.ute.foodiedash.infrastructure.persistence.common.jpa.entity.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "restaurant_pauses")
public class RestaurantPauseJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;

    @Column(length = 255, nullable = false)
    private String reason;

    @Column(name = "paused_from", nullable = false)
    private Instant pausedFrom;

    @Column(name = "paused_to", nullable = false)
    private Instant pausedTo;
}
