package com.ute.foodiedash.application.menu.query;

import java.math.BigDecimal;

public record MenuItemOptionValueQueryResult(
    Long id,
    Long optionId,
    String name,
    BigDecimal extraPrice
) {}
