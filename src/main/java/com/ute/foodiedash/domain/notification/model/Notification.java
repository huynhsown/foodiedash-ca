package com.ute.foodiedash.domain.notification.model;

import com.ute.foodiedash.domain.common.model.BaseEntity;
import com.ute.foodiedash.domain.notification.enums.NotificationRole;
import com.ute.foodiedash.domain.notification.enums.NotificationType;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

@Getter
public class Notification extends BaseEntity {

    private Long id;
    private Long recipientUserId;
    private NotificationRole recipientRole;
    private NotificationType type;

    private String titleKey;
    private String bodyKey;
    private Map<String, Object> payload;

    private String dedupeKey;
    private Instant readAt;

    public boolean isRead() {
        return readAt != null;
    }

    public static Notification create(
            Long recipientUserId,
            NotificationRole recipientRole,
            NotificationType type,
            String titleKey,
            String bodyKey,
            Map<String, Object> payload,
            String dedupeKey
    ) {
        if (recipientUserId == null) {
            throw new IllegalArgumentException("recipientUserId required");
        }
        if (recipientRole == null) {
            throw new IllegalArgumentException("recipientRole required");
        }
        if (type == null) {
            throw new IllegalArgumentException("type required");
        }
        if (titleKey == null || titleKey.isBlank()) {
            throw new IllegalArgumentException("titleKey required");
        }
        if (bodyKey == null || bodyKey.isBlank()) {
            throw new IllegalArgumentException("bodyKey required");
        }
        if (dedupeKey == null || dedupeKey.isBlank()) {
            throw new IllegalArgumentException("dedupeKey required");
        }

        Notification notification = new Notification();
        notification.recipientUserId = recipientUserId;
        notification.recipientRole = recipientRole;
        notification.type = type;
        notification.titleKey = titleKey;
        notification.bodyKey = bodyKey;
        notification.payload = payload;
        notification.dedupeKey = dedupeKey;
        notification.readAt = null;
        return notification;
    }

    public static Notification reconstruct(
            Long id,
            Long recipientUserId,
            NotificationRole recipientRole,
            NotificationType type,
            String titleKey,
            String bodyKey,
            Map<String, Object> payload,
            String dedupeKey,
            Instant readAt,
            Instant createdAt,
            Instant updatedAt,
            String createdBy,
            String updatedBy,
            Instant deletedAt,
            Long version
    ) {
        Notification notification = new Notification();
        notification.id = id;
        notification.recipientUserId = recipientUserId;
        notification.recipientRole = recipientRole;
        notification.type = type;
        notification.titleKey = titleKey;
        notification.bodyKey = bodyKey;
        notification.payload = payload;
        notification.dedupeKey = dedupeKey;
        notification.readAt = readAt;
        notification.restoreAudit(createdAt, updatedAt, createdBy, updatedBy, deletedAt, version);
        return notification;
    }

    public void markRead() {
        if (readAt != null) {
            return;
        }
        this.readAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

