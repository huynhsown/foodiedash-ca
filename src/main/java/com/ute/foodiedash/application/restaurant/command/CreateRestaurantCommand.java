package com.ute.foodiedash.application.restaurant.command;

import java.math.BigDecimal;

public record CreateRestaurantCommand(
    String code,
    String slug,
    String name,
    String description,
    String address,
    String phone,
    BigDecimal lat,
    BigDecimal lng,
    String status
) {}
