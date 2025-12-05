package com.ute.foodiedash.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cart_item_options")
public class CartItemOptionJpaEntity extends BaseJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cart_item_id", nullable = false)
    private Long cartItemId;

    @Column(name = "option_id", nullable = false)
    private Long optionId;

    @Column(name = "option_name", nullable = false)
    private String optionName;

    @Column(name = "required", nullable = false)
    private Boolean required;

    @Column(name = "min_value")
    private Integer minValue;

    @Column(name = "max_value")
    private Integer maxValue;
}
