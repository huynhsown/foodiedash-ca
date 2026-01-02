package com.ute.foodiedash.application.order.service;

import com.ute.foodiedash.application.notification.command.CreateNotificationCommand;
import com.ute.foodiedash.application.notification.usecase.CreateNotificationUseCase;
import com.ute.foodiedash.application.order.port.OrderCustomerNotificationPort;
import com.ute.foodiedash.domain.notification.enums.NotificationRole;
import com.ute.foodiedash.domain.notification.enums.NotificationType;
import com.ute.foodiedash.domain.order.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OrderCustomerNotificationService implements OrderCustomerNotificationPort {

    private final CreateNotificationUseCase createNotificationUseCase;

    @Override
    public void notifyForOrderStatus(Order order, NotificationType type) {
        if (order == null || order.getCustomerId() == null) {
            return;
        }

        Keys keys = keysFor(type);
        Map<String, Object> payload = buildPayload(order);

        createNotificationUseCase.execute(
                new CreateNotificationCommand(
                        order.getCustomerId(),
                        NotificationRole.CUSTOMER,
                        type,
                        keys.titleKey(),
                        keys.bodyKey(),
                        payload,
                        "order-" + order.getId() + "-" + type.name()
                )
        );
    }

    private static Map<String, Object> buildPayload(Order order) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("orderId", order.getId());
        payload.put("orderCode", order.getCode());
        if (order.getStatus() != null) {
            payload.put("orderStatus", order.getStatus().name());
        }
        return payload;
    }

    private static Keys keysFor(NotificationType type) {
        return switch (type) {
            case ORDER_ACCEPTED -> new Keys(
                    "notification.order.accepted.title",
                    "notification.order.accepted.body");
            case ORDER_PREPARING -> new Keys(
                    "notification.order.preparing.title",
                    "notification.order.preparing.body");
            case ORDER_READY -> new Keys(
                    "notification.order.ready.title",
                    "notification.order.ready.body");
            case ORDER_ASSIGNED -> new Keys(
                    "notification.order.assigned.title",
                    "notification.order.assigned.body");
            case ORDER_PICKED_UP -> new Keys(
                    "notification.order.picked_up.title",
                    "notification.order.picked_up.body");
            case ORDER_DELIVERED -> new Keys(
                    "notification.order.delivered.title",
                    "notification.order.delivered.body");
            case ORDER_COMPLETED -> new Keys(
                    "notification.order.completed.title",
                    "notification.order.completed.body");
            case ORDER_CANCELLED -> new Keys(
                    "notification.order.cancelled.title",
                    "notification.order.cancelled.body");
        };
    }

    private record Keys(String titleKey, String bodyKey) {}
}
