package com.ute.foodiedash.interfaces.rest.menu.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MenuItemOptionResponseDTO {
    private Long id;
    private Long menuItemId;
    private String name;
    private Boolean required;
    private Integer minValue;
    private Integer maxValue;
    private List<MenuItemOptionValueResponseDTO> itemOptionValues;
}
