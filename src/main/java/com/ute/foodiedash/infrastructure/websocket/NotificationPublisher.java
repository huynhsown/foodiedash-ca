package com.ute.foodiedash.infrastructure.websocket;

import com.ute.foodiedash.application.notification.port.NotificationPort;
import com.ute.foodiedash.domain.notification.enums.NotificationRole;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationPublisher implements NotificationPort {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void pushToUser(Long recipientUserId, NotificationRole recipientRole, Object payload) {
        if (recipientUserId == null || payload == null) {
            return;
        }

        messagingTemplate.convertAndSendToUser(
                recipientUserId.toString(),
                "/queue/notifications",
                payload
        );
    }
}
