package com.ute.foodiedash.infrastructure.notification;

import com.ute.foodiedash.application.notification.event.NotificationCreatedEvent;
import com.ute.foodiedash.application.notification.port.NotificationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationCreatedEventListener {
    private final NotificationPort notificationPort;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onNotificationCreated(NotificationCreatedEvent event) {
        notificationPort.pushToUser(
                event.getRecipientUserId(),
                event.getRecipientRole(),
                event.getPayload()
        );
    }
}
