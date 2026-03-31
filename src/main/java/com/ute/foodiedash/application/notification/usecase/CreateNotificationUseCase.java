package com.ute.foodiedash.application.notification.usecase;

import com.ute.foodiedash.application.common.port.DomainEventPublisher;
import com.ute.foodiedash.application.notification.command.CreateNotificationCommand;
import com.ute.foodiedash.application.notification.event.NotificationCreatedEvent;
import com.ute.foodiedash.application.notification.query.NotificationResult;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.notification.model.Notification;
import com.ute.foodiedash.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateNotificationUseCase {

    private final NotificationRepository notificationRepository;
    private final DomainEventPublisher domainEventPublisher;

    @Transactional
    public NotificationResult execute(CreateNotificationCommand command) {
        if (command == null) {
            throw new BadRequestException("Notification command required");
        }
        if (command.recipientUserId() == null) {
            throw new BadRequestException("recipientUserId required");
        }
        if (command.recipientRole() == null) {
            throw new BadRequestException("recipientRole required");
        }
        if (command.type() == null) {
            throw new BadRequestException("type required");
        }
        if (command.dedupeKey() == null || command.dedupeKey().isBlank()) {
            throw new BadRequestException("dedupeKey required");
        }

        var existing = notificationRepository.findByRecipientUserIdAndRecipientRoleAndTypeAndDedupeKey(
                command.recipientUserId(),
                command.recipientRole(),
                command.type(),
                command.dedupeKey()
        );
        if (existing.isPresent()) {
            return toResult(existing.get());
        }

        Notification notification = Notification.create(
                command.recipientUserId(),
                command.recipientRole(),
                command.type(),
                command.titleKey(),
                command.bodyKey(),
                command.payload(),
                command.dedupeKey()
        );

        Notification saved = notificationRepository.save(notification);

        NotificationResult result = toResult(saved);

        domainEventPublisher.publish(new NotificationCreatedEvent(
                this,
                saved.getRecipientUserId(),
                saved.getRecipientRole(),
                result
        ));


        return result;
    }

    private NotificationResult toResult(Notification n) {
        return new NotificationResult(
                n.getId(),
                n.getType().name(),
                n.getTitleKey(),
                n.getBodyKey(),
                n.getPayload(),
                n.isRead(),
                n.getReadAt(),
                n.getCreatedAt()
        );
    }
}

