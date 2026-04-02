package com.ute.foodiedash.infrastructure.persistence.notification.jpa.repository;

import com.ute.foodiedash.domain.notification.enums.NotificationRole;
import com.ute.foodiedash.domain.notification.enums.NotificationType;
import com.ute.foodiedash.infrastructure.persistence.notification.jpa.entity.NotificationJpaEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationJpaRepository extends JpaRepository<NotificationJpaEntity, Long> {

    Optional<NotificationJpaEntity> findByRecipientUserIdAndRecipientRoleAndTypeAndDedupeKey(
            Long recipientUserId,
            NotificationRole recipientRole,
            NotificationType type,
            String dedupeKey
    );

    List<NotificationJpaEntity> findByRecipientUserIdAndRecipientRoleOrderByCreatedAtDesc(
            Long recipientUserId,
            NotificationRole recipientRole,
            Pageable pageable
    );

    int countByRecipientUserIdAndRecipientRole(Long recipientUserId, NotificationRole recipientRole);

    List<NotificationJpaEntity> findByIdInAndRecipientUserIdAndRecipientRole(
            List<Long> ids,
            Long recipientUserId,
            NotificationRole recipientRole
    );
}

