package com.ute.foodiedash.application.order.port;

import com.ute.foodiedash.domain.order.model.Coordinate;
import com.ute.foodiedash.application.order.query.RouteQueryResult;

public interface RouteCalculationPort {
    RouteQueryResult calculateRoute(Coordinate start, Coordinate end);
}
