package com.ute.foodiedash.application.order.query;

import com.ute.foodiedash.domain.order.model.OrderItemOptionValue;

import java.math.BigDecimal;

public record OptionValueResult(
        Long optionValueId,
        Long sourceOptionValueId,
        String optionValueName,
        Integer quantity,
        BigDecimal extraPrice
) {
    public static OptionValueResult from(OrderItemOptionValue value) {
        return new OptionValueResult(
                value.getId(),
                value.getOptionValueId(),
                value.getOptionValueName(),
                value.getQuantity(),
                value.getExtraPrice()
        );
    }
}
