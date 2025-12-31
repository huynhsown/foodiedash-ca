package com.ute.foodiedash.application.notification.query;

import java.time.Instant;
import java.util.Map;

public record NotificationResult(
        Long id,
        String type,
        String titleKey,
        String bodyKey,
        Map<String, Object> payload,
        Boolean read,
        Instant readAt,
        Instant createdAt
) {
}

