package com.ute.foodiedash.interfaces.rest.menu.dto;

import com.ute.foodiedash.domain.menu.enums.MenuItemStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class MenuItemResponseDTO {
    private Long id;
    private Long menuId;
    private Long restaurantId;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private MenuItemStatus status;
    private List<MenuItemOptionResponseDTO> itemOptions;
}
