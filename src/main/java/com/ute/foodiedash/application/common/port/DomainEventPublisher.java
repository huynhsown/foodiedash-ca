package com.ute.foodiedash.application.common.port;

public interface DomainEventPublisher {
    void publish(Object event);
}
