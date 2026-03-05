package com.ute.foodiedash.interfaces.rest.promotion.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class AddPromotionRestaurantsRequestDTO {
    @NotEmpty
    private List<Long> restaurantIds;
}
