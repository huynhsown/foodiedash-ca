package com.ute.foodiedash.application.order.query;

import com.ute.foodiedash.domain.order.model.Coordinate;

import java.math.BigDecimal;
import java.util.List;

public record CoordinateResult(
        BigDecimal lat,
        BigDecimal lng
) {
    public static CoordinateResult from(Coordinate c) {
        if (c == null) {
            return null;
        }
        return new CoordinateResult(c.lat(), c.lng());
    }

    public static List<CoordinateResult> listFrom(List<Coordinate> geometry) {
        if (geometry == null) {
            return List.of();
        }
        return geometry.stream()
                .map(CoordinateResult::from)
                .toList();
    }
}
