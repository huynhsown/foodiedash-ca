package com.ute.foodiedash.application.order.query;

import java.util.List;

public record RouteQueryResult(
        double distance,
        int etaInMinutes,
        List<Coordinate> geometry
) {}

