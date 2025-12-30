package com.ute.foodiedash.application.order.query;

import com.ute.foodiedash.domain.order.model.OrderStatusHistory;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

public record StatusHistoryResult(
        Long statusHistoryId,
        String status,
        String note,
        Instant createdAt
) {
    public static StatusHistoryResult from(OrderStatusHistory h) {
        return new StatusHistoryResult(
                h.getId(),
                h.getStatus() != null ? h.getStatus().name() : null,
                h.getNote(),
                h.getCreatedAt()
        );
    }

    public static List<StatusHistoryResult> listFromSorted(List<OrderStatusHistory> histories) {
        if (histories == null) {
            return List.of();
        }
        return histories.stream()
                .sorted(Comparator.comparing(OrderStatusHistory::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(StatusHistoryResult::from)
                .toList();
    }
}
