package com.ute.foodiedash.domain.menu.model;

import com.ute.foodiedash.domain.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MenuItemOptionValue extends BaseEntity {
    private Long id;
    private Long optionId;
    private String name;
    private BigDecimal extraPrice;
}
