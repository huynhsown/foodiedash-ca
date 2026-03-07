package com.ute.foodiedash.infrastructure.persistence.cart.jpa.entity;

import com.ute.foodiedash.infrastructure.persistence.common.jpa.entity.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "cart_item_option_values")
public class CartItemOptionValueJpaEntity extends BaseJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cart_item_option_id", nullable = false)
    private Long cartItemOptionId;

    @Column(name = "option_value_id", nullable = false)
    private Long optionValueId;

    @Column(name = "option_value_name", length = 255)
    private String optionValueName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "extra_price", precision = 12, scale = 2, nullable = false)
    private BigDecimal extraPrice;
}
