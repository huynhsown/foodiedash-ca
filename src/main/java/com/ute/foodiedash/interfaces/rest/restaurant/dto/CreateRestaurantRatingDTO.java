package com.ute.foodiedash.interfaces.rest.restaurant.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateRestaurantRatingDTO {

    @NotNull(message = "Restaurant ID is required")
    private Long restaurantId;

    @NotNull(message = "Rating average is required")
    @Positive(message = "Rating average must be positive")
    private BigDecimal ratingAvg;

    @NotNull(message = "Rating count is required")
    @Positive(message = "Rating count must be positive")
    private Integer ratingCount;
}
