package com.ute.foodiedash.infrastructure.messaging;

import com.ute.foodiedash.application.common.port.DomainEventPublisher;
import com.ute.foodiedash.application.order.event.OrderMarkedReadyEvent;
import com.ute.foodiedash.domain.restaurant.event.RestaurantCreatedEvent;
import com.ute.foodiedash.domain.restaurant.event.RestaurantDeletedEvent;
import com.ute.foodiedash.domain.restaurant.event.RestaurantUpdatedEvent;
import com.ute.foodiedash.infrastructure.config.RabbitMQProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DomainEventPublisherAdapter implements DomainEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties properties;

    private static final Map<Class<?>, String> ROUTING_KEY_MAP = Map.of(
            RestaurantCreatedEvent.class, "restaurant.created",
            RestaurantUpdatedEvent.class, "restaurant.updated",
            RestaurantDeletedEvent.class, "restaurant.deleted",
            OrderMarkedReadyEvent.class, "order.ready"
    );

    @Override
    public void publish(Object event) {
        String routingKey = ROUTING_KEY_MAP.get(event.getClass());
        if (routingKey == null) {
            log.warn("No routing key mapping found for event type: {}", event.getClass().getName());
            return;
        }
        log.info("Publishing event type={}, routingKey={}, exchange={}", event.getClass().getSimpleName(), routingKey, properties.getDomainExchange());
        rabbitTemplate.convertAndSend(properties.getDomainExchange(), routingKey, event);
    }
}
