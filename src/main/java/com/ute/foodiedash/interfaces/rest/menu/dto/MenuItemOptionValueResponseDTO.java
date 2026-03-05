package com.ute.foodiedash.interfaces.rest.menu.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MenuItemOptionValueResponseDTO {
    private Long id;
    private Long optionId;
    private String name;
    private BigDecimal extraPrice;
}
