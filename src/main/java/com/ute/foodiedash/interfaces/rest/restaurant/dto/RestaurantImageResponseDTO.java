package com.ute.foodiedash.interfaces.rest.restaurant.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantImageResponseDTO {

    private Long id;
    private Long restaurantId;
    private String imageUrl;
}
