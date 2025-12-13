package com.ute.foodiedash.infrastructure.persistence.cart.jpa.entity;

import com.ute.foodiedash.infrastructure.persistence.common.jpa.entity.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "cart_item_options")
public class CartItemOptionJpaEntity extends BaseJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_item_id")
    private CartItemJpaEntity cartItem;

    @OneToMany(
        mappedBy = "cartItemOption",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Set<CartItemOptionValueJpaEntity> values = new HashSet<>();
}
