package com.ute.foodiedash.application.common.port;

public interface EventPublisherPort {
    void publish(Object event);
}
