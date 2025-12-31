package com.ute.foodiedash.domain.notification.repository;

import com.ute.foodiedash.domain.notification.enums.NotificationRole;
import com.ute.foodiedash.domain.notification.enums.NotificationType;
import com.ute.foodiedash.domain.notification.model.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    Notification save(Notification notification);

    Optional<Notification> findByRecipientUserIdAndRecipientRoleAndTypeAndDedupeKey(
            Long recipientUserId,
            NotificationRole recipientRole,
            NotificationType type,
            String dedupeKey
    );

    List<Notification> findByRecipientUserIdAndRecipientRoleOrderByCreatedAtDesc(
            Long recipientUserId,
            NotificationRole recipientRole,
            int limit,
            int offset
    );

    long countByRecipientUserIdAndRecipientRole(Long recipientUserId, NotificationRole recipientRole);

    List<Notification> findByIdInAndRecipientUserIdAndRecipientRole(
            List<Long> ids,
            Long recipientUserId,
            NotificationRole recipientRole
    );
}

