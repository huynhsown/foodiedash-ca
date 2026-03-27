package com.ute.foodiedash.domain.order.model;

import java.math.BigDecimal;

public record Coordinate(
        BigDecimal lat,
        BigDecimal lng
) {}
