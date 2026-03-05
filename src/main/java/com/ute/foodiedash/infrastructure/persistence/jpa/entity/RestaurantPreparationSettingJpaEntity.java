package com.ute.foodiedash.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "restaurant_preparation_settings")
public class RestaurantPreparationSettingJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;

    @Column(name = "prep_time_min", nullable = false)
    private Integer prepTimeMin;

    @Column(name = "prep_time_max", nullable = false)
    private Integer prepTimeMax;

    @Column(name = "slot_duration", nullable = false)
    private Integer slotDuration;

    @Column(name = "max_orders_per_slot", nullable = false)
    private Integer maxOrdersPerSlot;
}
