package com.ute.foodiedash.interfaces.rest.restaurant.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantCategoryResponseDTO {

    private Long id;
    private String name;
    private String iconUrl;
    private String description;
}
