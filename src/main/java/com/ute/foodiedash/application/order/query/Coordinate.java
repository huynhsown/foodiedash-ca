package com.ute.foodiedash.application.order.query;

import java.math.BigDecimal;

public record Coordinate(
        BigDecimal lat,
        BigDecimal lng
) {}
