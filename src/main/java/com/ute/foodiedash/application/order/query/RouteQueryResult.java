package com.ute.foodiedash.application.order.query;

import com.ute.foodiedash.domain.order.model.Coordinate;

import java.util.List;

public record RouteQueryResult(
        double distance,
        int etaInMinutes,
        List<Coordinate> geometry
) {}

