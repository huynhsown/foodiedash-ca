package com.ute.foodiedash.application.notification.usecase;

import com.ute.foodiedash.application.notification.command.MarkNotificationsReadCommand;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.exception.ForbiddenException;
import com.ute.foodiedash.domain.notification.model.Notification;
import com.ute.foodiedash.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MarkNotificationsReadUseCase {

    private final NotificationRepository notificationRepository;

    @Transactional
    public long execute(MarkNotificationsReadCommand command) {
        if (command == null) {
            throw new BadRequestException("Mark read command required");
        }
        if (command.recipientUserId() == null) {
            throw new BadRequestException("recipientUserId required");
        }
        if (command.recipientRole() == null) {
            throw new BadRequestException("recipientRole required");
        }
        if (command.notificationIds() == null || command.notificationIds().isEmpty()) {
            throw new BadRequestException("notificationIds required");
        }

        List<Long> ids = command.notificationIds();

        List<Notification> notifications = notificationRepository.findByIdInAndRecipientUserIdAndRecipientRole(
                ids,
                command.recipientUserId(),
                command.recipientRole()
        );

        if (notifications.size() != ids.size()) {
            throw new ForbiddenException("Notifications not found in your scope");
        }

        long changed = 0;
        for (Notification n : notifications) {
            boolean alreadyRead = n.isRead();
            n.markRead();
            if (!alreadyRead && n.isRead()) {
                notificationRepository.save(n);
                changed++;
            } else if (!alreadyRead) {
                notificationRepository.save(n);
                changed++;
            }
        }
        return changed;
    }
}

