package com.ute.foodiedash.infrastructure.order;

import com.ute.foodiedash.application.order.event.OrderMarkedReadyEvent;
import com.ute.foodiedash.application.order.usecase.AutoAssignDriverOnReadyUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderMarkedReadyEventListener {

    private final AutoAssignDriverOnReadyUseCase autoAssignDriverOnReadyUseCase;

    @RabbitListener(queues = "${rabbitmq.domain-queues.order-ready}")
    public void onOrderMarkedReady(OrderMarkedReadyEvent event) {
        log.debug("Received OrderMarkedReadyEvent for orderId={}", event.getOrderId());
        try {
            autoAssignDriverOnReadyUseCase.execute(event.getOrderId());
            log.info("Successfully processed auto-assign driver for orderId={}", event.getOrderId());
        } catch (Exception e) {
            log.error("Failed to auto-assign driver for orderId={}", event.getOrderId(), e);
        }
    }
}
