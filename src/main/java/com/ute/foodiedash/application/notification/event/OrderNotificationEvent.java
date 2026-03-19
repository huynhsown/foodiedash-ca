package com.ute.foodiedash.application.notification.event;

import com.ute.foodiedash.domain.notification.enums.NotificationRole;
import com.ute.foodiedash.domain.notification.enums.NotificationType;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderNotificationEvent {

    private Long recipientUserId;
    private NotificationRole recipientRole;
    private NotificationType type;
    private String titleKey;
    private String bodyKey;
    private Map<String, Object> payload;
    private String dedupeKey;
}
