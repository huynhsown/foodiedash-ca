package com.ute.foodiedash.application.order.port.model;

import com.ute.foodiedash.domain.order.enums.OrderStatus;

public record OrderValidation(
        Long orderId,
        Long customerId,
        Long restaurantId,
        OrderStatus status
) {}
