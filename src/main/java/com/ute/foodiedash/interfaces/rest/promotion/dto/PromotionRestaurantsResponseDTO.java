package com.ute.foodiedash.interfaces.rest.promotion.dto;

import lombok.Data;

import java.util.List;

@Data
public class PromotionRestaurantsResponseDTO {
    private Long promotionId;
    private List<Long> restaurantIds;
}
