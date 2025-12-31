package com.ute.foodiedash.application.notification.port;

import com.ute.foodiedash.domain.notification.enums.NotificationRole;

public interface NotificationPort {
    void pushToUser(Long recipientUserId, NotificationRole recipientRole, Object payload);
}
