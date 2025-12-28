package com.ute.foodiedash.application.order.query;

import com.ute.foodiedash.domain.order.model.OrderItem;
import com.ute.foodiedash.domain.order.model.OrderItemOption;

import java.math.BigDecimal;
import java.util.List;

public record ItemResult(
        Long itemId,
        Long menuItemId,
        String name,
        String imageUrl,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice,
        String notes,
        List<OptionResult> options
) {
    public static ItemResult from(OrderItem item) {
        return new ItemResult(
                item.getId(),
                item.getMenuItemId(),
                item.getName(),
                item.getImageUrl(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalPrice(),
                item.getNotes(),
                listOptions(item.getOptions())
        );
    }

    public static List<ItemResult> listFrom(List<OrderItem> items) {
        if (items == null) {
            return List.of();
        }
        return items.stream()
                .map(ItemResult::from)
                .toList();
    }

    private static List<OptionResult> listOptions(List<OrderItemOption> options) {
        if (options == null) {
            return List.of();
        }
        return options.stream()
                .map(OptionResult::from)
                .toList();
    }
}
