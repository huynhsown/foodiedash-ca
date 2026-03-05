package com.ute.foodiedash.application.cart.query;

import java.math.BigDecimal;

public record CartItemOptionValueQueryResult(
    Long id,
    Long optionValueId,
    String optionValueName,
    Integer quantity,
    BigDecimal extraPrice
) {}
