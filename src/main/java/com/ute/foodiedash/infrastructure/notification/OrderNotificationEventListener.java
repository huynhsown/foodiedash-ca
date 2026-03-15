package com.ute.foodiedash.infrastructure.notification;

import com.ute.foodiedash.application.notification.command.CreateNotificationCommand;
import com.ute.foodiedash.application.notification.event.OrderNotificationEvent;
import com.ute.foodiedash.application.notification.usecase.CreateNotificationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderNotificationEventListener {

    private final CreateNotificationUseCase createNotificationUseCase;

    @RabbitListener(queues = "${rabbitmq.queues.notification}")
    public void onOrderNotification(OrderNotificationEvent event) {
        createNotificationUseCase.execute(new CreateNotificationCommand(
                event.getRecipientUserId(),
                event.getRecipientRole(),
                event.getType(),
                event.getTitleKey(),
                event.getBodyKey(),
                event.getPayload(),
                event.getDedupeKey()
        ));
    }
}
