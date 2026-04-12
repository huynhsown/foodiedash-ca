package com.ute.foodiedash.application.notification.usecase;

import com.ute.foodiedash.application.notification.query.NotificationCountQueryResult;
import com.ute.foodiedash.domain.notification.enums.NotificationRole;
import com.ute.foodiedash.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetNotificationCountUseCase {
    private final NotificationRepository notificationRepository;

    public NotificationCountQueryResult execute(Long userId, NotificationRole role) {
        int totalItems = notificationRepository.countByRecipientUserIdAndRecipientRole(
                userId,
                role
        );
        return new NotificationCountQueryResult(totalItems);
    }
}
