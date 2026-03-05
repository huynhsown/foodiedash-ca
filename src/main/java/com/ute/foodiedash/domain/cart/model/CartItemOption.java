package com.ute.foodiedash.domain.cart.model;

import com.ute.foodiedash.domain.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemOption extends BaseEntity {
    private Long id;
    private Long cartItemId;
    private Long optionId;
    private String optionName;
    private Boolean required;
    private Integer minValue;
    private Integer maxValue;
}
