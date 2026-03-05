package com.ute.foodiedash.application.promotion.command;

import java.util.List;

public record AddPromotionRestaurantsCommand(
    List<Long> restaurantIds
) {}
