package com.ute.foodiedash.application.order.query;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderSummaryQueryResult(
        Long orderId,
        String orderCode,
        String status,
        BigDecimal subtotal,
        BigDecimal discount,
        BigDecimal deliveryFee,
        BigDecimal total,
        LocalDateTime placedAt,
        LocalDateTime acceptedAt,
        LocalDateTime preparedAt,
        LocalDateTime cancelledAt,
        LocalDateTime completeAt
) {}

