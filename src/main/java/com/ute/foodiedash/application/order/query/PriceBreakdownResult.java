package com.ute.foodiedash.application.order.query;

import java.math.BigDecimal;

public record PriceBreakdownResult(
        BigDecimal baseSubtotal,
        BigDecimal extrasSubtotal,
        BigDecimal itemsSubtotal,
        BigDecimal discount,
        BigDecimal deliveryFee,
        BigDecimal total
) {}

