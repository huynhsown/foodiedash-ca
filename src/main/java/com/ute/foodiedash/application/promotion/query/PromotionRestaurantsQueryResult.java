package com.ute.foodiedash.application.promotion.query;

import java.util.List;

public record PromotionRestaurantsQueryResult(
    Long promotionId,
    List<Long> restaurantIds
) {}
