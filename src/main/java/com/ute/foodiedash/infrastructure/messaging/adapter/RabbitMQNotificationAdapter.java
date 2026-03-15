package com.ute.foodiedash.infrastructure.messaging.adapter;

import com.ute.foodiedash.application.common.port.EventPublisherPort;
import com.ute.foodiedash.infrastructure.config.RabbitMQProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQNotificationAdapter implements EventPublisherPort {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties properties;

    @Override
    public void publish(Object event) {
        rabbitTemplate.convertAndSend(
                properties.getExchange(),
                properties.getRoutingKeys().getNotificationCreated(),
                event
        );
    }
}
