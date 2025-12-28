package com.ute.foodiedash.application.order.query;

import com.ute.foodiedash.domain.order.model.OrderItemOption;
import com.ute.foodiedash.domain.order.model.OrderItemOptionValue;

import java.util.List;

public record OptionResult(
        Long optionId,
        Long sourceOptionId,
        String optionName,
        Boolean required,
        Integer minValue,
        Integer maxValue,
        List<OptionValueResult> values
) {
    public static OptionResult from(OrderItemOption option) {
        return new OptionResult(
                option.getId(),
                option.getOptionId(),
                option.getOptionName(),
                option.getRequired(),
                option.getMinValue(),
                option.getMaxValue(),
                listOptionValues(option.getValues())
        );
    }

    private static List<OptionValueResult> listOptionValues(List<OrderItemOptionValue> values) {
        if (values == null) {
            return List.of();
        }
        return values.stream()
                .map(OptionValueResult::from)
                .toList();
    }
}
