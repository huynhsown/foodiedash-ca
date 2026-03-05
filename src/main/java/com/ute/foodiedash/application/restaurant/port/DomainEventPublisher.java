package com.ute.foodiedash.application.restaurant.port;

public interface DomainEventPublisher {
    void publish(Object event);
}
