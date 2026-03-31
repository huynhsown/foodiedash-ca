package com.ute.foodiedash.application.notification.usecase;

import com.ute.foodiedash.application.notification.query.ListNotificationsQueryResult;
import com.ute.foodiedash.application.notification.query.NotificationResult;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.notification.enums.NotificationRole;
import com.ute.foodiedash.domain.notification.model.Notification;
import com.ute.foodiedash.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListNotificationsUseCase {

    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public ListNotificationsQueryResult execute(
            Long recipientUserId,
            NotificationRole recipientRole,
            int page,
            int size
    ) {
        if (recipientUserId == null) {
            throw new BadRequestException("recipientUserId required");
        }
        if (recipientRole == null) {
            throw new BadRequestException("recipientRole required");
        }
        if (page < 0) {
            throw new BadRequestException("page must be >= 0");
        }
        if (size <= 0) {
            throw new BadRequestException("size must be > 0");
        }

        int offset = page * size;

        List<Notification> notifications = notificationRepository
                .findByRecipientUserIdAndRecipientRoleOrderByCreatedAtDesc(recipientUserId, recipientRole, size, offset);
        long total = notificationRepository.countByRecipientUserIdAndRecipientRole(recipientUserId, recipientRole);

        List<NotificationResult> results = notifications.stream()
                .map(n -> new NotificationResult(
                        n.getId(),
                        n.getType().name(),
                        n.getTitleKey(),
                        n.getBodyKey(),
                        n.getPayload(),
                        n.isRead(),
                        n.getReadAt(),
                        n.getCreatedAt()
                ))
                .toList();

        return new ListNotificationsQueryResult(results, total, page, size);
    }
}

