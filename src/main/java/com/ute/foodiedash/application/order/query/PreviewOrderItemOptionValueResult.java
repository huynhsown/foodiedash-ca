package com.ute.foodiedash.application.order.query;

import java.math.BigDecimal;

public record PreviewOrderItemOptionValueResult(
        Long optionValueId,
        String optionValueName,
        Integer quantity,
        BigDecimal extraPrice,
        BigDecimal totalExtraPrice
) {}

