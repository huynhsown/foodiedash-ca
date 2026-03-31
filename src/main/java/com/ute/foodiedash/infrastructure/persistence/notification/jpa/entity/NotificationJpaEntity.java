package com.ute.foodiedash.infrastructure.persistence.notification.jpa.entity;

import com.ute.foodiedash.domain.notification.enums.NotificationRole;
import com.ute.foodiedash.domain.notification.enums.NotificationType;
import com.ute.foodiedash.infrastructure.persistence.common.jpa.entity.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(
        name = "notifications",
        indexes = {
                @Index(name = "idx_notifications_recipient_created_at", columnList = "recipient_user_id, created_at")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_notification_recipient_type_dedupe",
                        columnNames = {"recipient_user_id", "recipient_role", "type", "dedupe_key"}
                )
        }
)
public class NotificationJpaEntity extends BaseJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipient_user_id", nullable = false)
    private Long recipientUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "recipient_role", nullable = false, length = 20)
    private NotificationRole recipientRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private NotificationType type;

    @Column(name = "title_key", nullable = false, length = 100)
    private String titleKey;

    @Column(name = "body_key", nullable = false, length = 100)
    private String bodyKey;

    @Column(name = "payload", columnDefinition = "text")
    private String payload;

    @Column(name = "dedupe_key", nullable = false, length = 200)
    private String dedupeKey;

    @Column(name = "read_at")
    private Instant readAt;
}

