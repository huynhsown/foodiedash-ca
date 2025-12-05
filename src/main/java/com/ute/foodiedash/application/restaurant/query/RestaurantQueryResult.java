package com.ute.foodiedash.application.restaurant.query;

import com.ute.foodiedash.domain.restaurant.enums.RestaurantStatus;

import java.math.BigDecimal;

public record RestaurantQueryResult(
    Long id,
    String code,
    String slug,
    String name,
    String description,
    String address,
    String phone,
    BigDecimal lat,
    BigDecimal lng,
    RestaurantStatus status
) {}
