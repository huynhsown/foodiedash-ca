package com.ute.foodiedash.application.order.port;

import com.ute.foodiedash.domain.notification.enums.NotificationType;
import com.ute.foodiedash.domain.order.model.Order;

public interface OrderCustomerNotificationPort {

    void notifyForOrderStatus(Order order, NotificationType type);
}
