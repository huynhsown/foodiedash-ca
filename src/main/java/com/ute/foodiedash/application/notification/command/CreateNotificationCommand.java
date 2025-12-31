package com.ute.foodiedash.application.notification.command;

import com.ute.foodiedash.domain.notification.enums.NotificationRole;
import com.ute.foodiedash.domain.notification.enums.NotificationType;

import java.util.Map;

public record CreateNotificationCommand(
        Long recipientUserId,
        NotificationRole recipientRole,
        NotificationType type,
        String titleKey,
        String bodyKey,
        Map<String, Object> payload,
        String dedupeKey
) {
}

