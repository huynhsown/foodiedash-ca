package com.ute.foodiedash.infrastructure.event;

import com.ute.foodiedash.application.restaurant.port.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringDomainEventPublisher implements DomainEventPublisher {
    private final ApplicationEventPublisher springEventPublisher;

    @Override
    public void publish(Object event) {
        springEventPublisher.publishEvent(event);
    }
}
