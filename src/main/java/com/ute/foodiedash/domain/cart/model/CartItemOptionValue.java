package com.ute.foodiedash.domain.cart.model;

import com.ute.foodiedash.domain.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartItemOptionValue extends BaseEntity {
    private Long id;
    private Long cartItemOptionId;
    private Long optionValueId;
    private String optionValueName;
    private Integer quantity;
    private BigDecimal extraPrice;
}
