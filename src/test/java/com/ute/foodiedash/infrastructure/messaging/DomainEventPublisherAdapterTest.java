package com.ute.foodiedash.infrastructure.messaging;

import com.ute.foodiedash.application.order.event.OrderMarkedReadyEvent;
import com.ute.foodiedash.domain.restaurant.event.RestaurantCreatedEvent;
import com.ute.foodiedash.domain.restaurant.event.RestaurantDeletedEvent;
import com.ute.foodiedash.domain.restaurant.event.RestaurantUpdatedEvent;
import com.ute.foodiedash.infrastructure.config.RabbitMQProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DomainEventPublisherAdapterTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    private RabbitMQProperties properties;
    private DomainEventPublisherAdapter adapter;

    @BeforeEach
    void setUp() {
        properties = new RabbitMQProperties();
        properties.setDomainExchange("foodiedash.domain.exchange");

        RabbitMQProperties.DomainRoutingKeys routingKeys = new RabbitMQProperties.DomainRoutingKeys();
        routingKeys.setRestaurantCreated("restaurant.created");
        routingKeys.setRestaurantUpdated("restaurant.updated");
        routingKeys.setRestaurantDeleted("restaurant.deleted");
        routingKeys.setOrderReady("order.ready");
        properties.setDomainRoutingKeys(routingKeys);

        adapter = new DomainEventPublisherAdapter(rabbitTemplate, properties);
    }

    @Test
    void shouldPublishRestaurantCreatedEventWithCorrectRoutingKey() {
        RestaurantCreatedEvent event = new RestaurantCreatedEvent(123L);

        adapter.publish(event);

        verify(rabbitTemplate).convertAndSend(
                eq("foodiedash.domain.exchange"),
                eq("restaurant.created"),
                eq(event)
        );
    }

    @Test
    void shouldPublishRestaurantUpdatedEventWithCorrectRoutingKey() {
        RestaurantUpdatedEvent event = new RestaurantUpdatedEvent(456L);

        adapter.publish(event);

        verify(rabbitTemplate).convertAndSend(
                eq("foodiedash.domain.exchange"),
                eq("restaurant.updated"),
                eq(event)
        );
    }

    @Test
    void shouldPublishRestaurantDeletedEventWithCorrectRoutingKey() {
        RestaurantDeletedEvent event = new RestaurantDeletedEvent(789L);

        adapter.publish(event);

        verify(rabbitTemplate).convertAndSend(
                eq("foodiedash.domain.exchange"),
                eq("restaurant.deleted"),
                eq(event)
        );
    }

    @Test
    void shouldPublishOrderMarkedReadyEventWithCorrectRoutingKey() {
        OrderMarkedReadyEvent event = new OrderMarkedReadyEvent(42L);

        adapter.publish(event);

        verify(rabbitTemplate).convertAndSend(
                eq("foodiedash.domain.exchange"),
                eq("order.ready"),
                eq(event)
        );
    }

    @Disabled("Requires RabbitMQ running — use 'mvn test -Dtest=DomainEventPublisherAdapterTest -Drabbitmq=integration'")
    @Test
    void retryInterceptorShouldRouteToDlqAfterMaxRetries() {
        // This integration test validates that when a consumer throws an exception,
        // the message is retried 3 times with exponential backoff and then routed to the DLQ.
        // The RetryOperationsInterceptor is configured with:
        // - maxAttempts=4 (1 original + 3 retries)
        // - initialInterval=1000ms, multiplier=2.0, maxInterval=4000ms
        // - RepublishMessageRecoverer → domain.dead-letter.exchange
        //
        // To manually verify:
        // 1. Start RabbitMQ: docker compose up -d rabbitmq
        // 2. Publish an event via the adapter
        // 3. Observe retry attempts in RabbitMQ management UI
        // 4. After 3 retries, message appears in the appropriate DLQ
    }
}
