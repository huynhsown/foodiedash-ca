package com.ute.foodiedash.application.notification.event;

import com.ute.foodiedash.application.notification.query.NotificationResult;
import com.ute.foodiedash.domain.notification.enums.NotificationRole;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NotificationCreatedEvent extends ApplicationEvent {

    private final Long recipientUserId;
    private final NotificationRole recipientRole;
    private final Object payload;

    public NotificationCreatedEvent(Object source, Long recipientUserId, NotificationRole recipientRole, Object payload) {
        super(source);
        this.recipientUserId = recipientUserId;
        this.recipientRole = recipientRole;
        this.payload = payload;
    }
}
