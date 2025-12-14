package com.ute.foodiedash.infrastructure.persistence.menu.jpa.entity;

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
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "menu_item_option_values")
public class MenuItemOptionValueJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private MenuItemOptionJpaEntity option;

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
