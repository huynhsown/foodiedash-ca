package com.ute.foodiedash.infrastructure.persistence.order.jpa.entity;

import com.ute.foodiedash.infrastructure.persistence.common.jpa.entity.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "order_item_option_values")
public class OrderItemOptionValueJpaEntity extends BaseJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_option_id", nullable = false)
    private OrderItemOptionJpaEntity orderItemOption;

    @Column(name = "option_value_id", nullable = false)
    private Long optionValueId;

    @Column(name = "option_value_name", nullable = false, length = 255)
    private String optionValueName;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "extra_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal extraPrice;
}
