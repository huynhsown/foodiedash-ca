package com.ute.foodiedash.interfaces.rest.menu.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RestaurantMenuResponseDTO {
    private List<MenuDetailDTO> menus;
}
