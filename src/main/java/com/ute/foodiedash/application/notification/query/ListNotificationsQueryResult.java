package com.ute.foodiedash.application.notification.query;

import java.util.List;

public record ListNotificationsQueryResult(
        List<NotificationResult> notifications,
        long totalElements,
        int page,
        int size
) {
}

