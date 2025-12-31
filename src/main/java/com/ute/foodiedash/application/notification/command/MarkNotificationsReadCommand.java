package com.ute.foodiedash.application.notification.command;

import com.ute.foodiedash.domain.notification.enums.NotificationRole;

import java.util.List;

public record MarkNotificationsReadCommand(
        Long recipientUserId,
        NotificationRole recipientRole,
        List<Long> notificationIds
) {
}

