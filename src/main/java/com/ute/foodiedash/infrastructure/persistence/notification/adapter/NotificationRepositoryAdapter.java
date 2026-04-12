package com.ute.foodiedash.infrastructure.persistence.notification.adapter;

import com.ute.foodiedash.domain.notification.enums.NotificationRole;
import com.ute.foodiedash.domain.notification.enums.NotificationType;
import com.ute.foodiedash.domain.notification.model.Notification;
import com.ute.foodiedash.domain.notification.repository.NotificationRepository;
import com.ute.foodiedash.infrastructure.persistence.notification.jpa.mapper.NotificationJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.notification.jpa.repository.NotificationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NotificationRepositoryAdapter implements NotificationRepository {

    private final NotificationJpaRepository jpaRepository;
    private final NotificationJpaMapper mapper;

    @Override
    public Notification save(Notification notification) {
        var entity = mapper.toJpaEntity(notification);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Notification> findByRecipientUserIdAndRecipientRoleAndTypeAndDedupeKey(
            Long recipientUserId,
            NotificationRole recipientRole,
            NotificationType type,
            String dedupeKey
    ) {
        return jpaRepository
                .findByRecipientUserIdAndRecipientRoleAndTypeAndDedupeKey(recipientUserId, recipientRole, type, dedupeKey)
                .map(mapper::toDomain);
    }

    @Override
    public List<Notification> findByRecipientUserIdAndRecipientRoleOrderByCreatedAtDesc(
            Long recipientUserId,
            NotificationRole recipientRole,
            int limit,
            int offset
    ) {
        if (limit <= 0) {
            return List.of();
        }
        if (offset < 0) {
            offset = 0;
        }

        int page = offset / limit;
        Pageable pageable = PageRequest.of(page, limit);
        return jpaRepository
                .findByRecipientUserIdAndRecipientRoleOrderByCreatedAtDesc(recipientUserId, recipientRole, pageable)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public int countByRecipientUserIdAndRecipientRole(Long recipientUserId, NotificationRole recipientRole) {
        return jpaRepository.countByRecipientUserIdAndRecipientRole(recipientUserId, recipientRole);
    }

    @Override
    public List<Notification> findByIdInAndRecipientUserIdAndRecipientRole(
            List<Long> ids,
            Long recipientUserId,
            NotificationRole recipientRole
    ) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return jpaRepository
                .findByIdInAndRecipientUserIdAndRecipientRole(ids, recipientUserId, recipientRole)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}

