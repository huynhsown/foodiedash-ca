package com.ute.foodiedash.interfaces.rest.restaurant.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RestaurantRatingResponseDTO {

    private Long id;
    private Long restaurantId;
    private BigDecimal ratingAvg;
    private Integer ratingCount;
}
