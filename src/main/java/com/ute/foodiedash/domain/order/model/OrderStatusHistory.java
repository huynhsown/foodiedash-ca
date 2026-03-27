package com.ute.foodiedash.domain.order.model;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.model.BaseEntity;
import com.ute.foodiedash.domain.order.enums.OrderStatus;
import lombok.Getter;
import java.time.Instant;

@Getter
public class OrderStatusHistory extends BaseEntity {
    private Long id;
    private Long orderId;
    private OrderStatus status;
    private String note;

    public static OrderStatusHistory create(
            Long orderId,
            OrderStatus status,
            String note
    ) {

        if (orderId == null) {
            throw new BadRequestException("Order id required");
        }

        if (status == null) {
            throw new BadRequestException("Status required");
        }

        OrderStatusHistory history = new OrderStatusHistory();
        history.orderId = orderId;
        history.status = status;
        history.note = note;

        return history;
    }

    public static OrderStatusHistory reconstruct(
            Long id,
            Long orderId,
            OrderStatus status,
            String note,
            Instant createdAt,
            Instant updatedAt,
            String createdBy,
            String updatedBy,
            Instant deletedAt,
            Long version
    ) {

        OrderStatusHistory history = new OrderStatusHistory();
        history.id = id;
        history.orderId = orderId;
        history.status = status;
        history.note = note;
        history.restoreAudit(createdAt, updatedAt, createdBy, updatedBy, deletedAt, version);

        return history;
    }
}
