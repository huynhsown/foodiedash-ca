package com.ute.foodiedash.application.order.query;

import java.util.List;

public record PreviewOrderItemOptionResult(
        Long optionId,
        String optionName,
        Boolean required,
        Integer minValue,
        Integer maxValue,
        List<PreviewOrderItemOptionValueResult> values
) {}

