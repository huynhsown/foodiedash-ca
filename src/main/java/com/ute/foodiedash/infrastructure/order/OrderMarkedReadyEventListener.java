package com.ute.foodiedash.infrastructure.order;

import com.ute.foodiedash.application.order.event.OrderMarkedReadyEvent;
import com.ute.foodiedash.application.order.usecase.AutoAssignDriverOnReadyUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderMarkedReadyEventListener {

    private final AutoAssignDriverOnReadyUseCase autoAssignDriverOnReadyUseCase;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderMarkedReady(OrderMarkedReadyEvent event) {
        autoAssignDriverOnReadyUseCase.execute(event.getOrderId());
    }
}
