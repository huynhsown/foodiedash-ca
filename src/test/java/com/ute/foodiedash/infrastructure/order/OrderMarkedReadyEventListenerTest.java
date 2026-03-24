package com.ute.foodiedash.infrastructure.order;

import com.ute.foodiedash.application.order.event.OrderMarkedReadyEvent;
import com.ute.foodiedash.application.order.usecase.AutoAssignDriverOnReadyUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderMarkedReadyEventListenerTest {

    @Mock
    private AutoAssignDriverOnReadyUseCase autoAssignDriverOnReadyUseCase;

    @InjectMocks
    private OrderMarkedReadyEventListener listener;

    @Test
    void onOrderMarkedReady_shouldDelegateToUseCase() {
        OrderMarkedReadyEvent event = new OrderMarkedReadyEvent(42L);

        listener.onOrderMarkedReady(event);

        verify(autoAssignDriverOnReadyUseCase).execute(42L);
    }
}
