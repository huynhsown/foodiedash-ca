package com.ute.foodiedash.application.order.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrderMarkedReadyEvent extends ApplicationEvent {

    private final Long orderId;

    public OrderMarkedReadyEvent(Object source, Long orderId) {
        super(source);
        this.orderId = orderId;
    }
}
