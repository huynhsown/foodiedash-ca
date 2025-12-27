package com.ute.foodiedash.application.order.command;

public record CancelOrderCommand(
        Long orderId,
        String reason
) {}

