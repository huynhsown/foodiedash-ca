package com.ute.foodiedash.application.order.query;

import java.util.List;

public record RouteQueryResult(
        double distance,
        double eta,
        List<Coordinate> geometry
) {}

