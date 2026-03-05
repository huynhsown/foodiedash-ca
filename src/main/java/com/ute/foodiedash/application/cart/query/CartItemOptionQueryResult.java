package com.ute.foodiedash.application.cart.query;

import java.util.List;

public record CartItemOptionQueryResult(
    Long id,
    Long optionId,
    String optionName,
    Boolean required,
    Integer minValue,
    Integer maxValue,
    List<CartItemOptionValueQueryResult> values
) {}
