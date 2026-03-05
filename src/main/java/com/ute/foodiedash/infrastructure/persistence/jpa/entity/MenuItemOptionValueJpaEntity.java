package com.ute.foodiedash.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "menu_item_option_values")
public class MenuItemOptionValueJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "option_id", nullable = false)
    private Long optionId;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(
            name = "extra_price",
            precision = 12,
            scale = 2,
            nullable = false
    )
    private BigDecimal extraPrice;
}
