package com.ute.foodiedash.application.order.query;

import java.math.BigDecimal;
import java.util.List;

public record PreviewOrderItemResult(
        Long menuItemId,
        String name,
        String imageUrl,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice,
        String notes,
        List<PreviewOrderItemOptionResult> options
) {}

