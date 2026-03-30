package com.ute.foodiedash.infrastructure.order;

import com.ute.foodiedash.application.order.port.DriverAssignmentPendingPort;
import com.ute.foodiedash.application.order.usecase.AutoAssignDriverOnReadyUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DriverAssignmentRetryScheduler {

    private final DriverAssignmentPendingPort driverAssignmentPendingPort;
    private final AutoAssignDriverOnReadyUseCase autoAssignDriverOnReadyUseCase;

    @Scheduled(fixedDelayString = "${driver.dispatch.retry-interval-ms:30000}")
    public void retryPendingAssignments() {
        for (Long orderId : driverAssignmentPendingPort.snapshotPendingOrderIds()) {
            autoAssignDriverOnReadyUseCase.execute(orderId);
        }
    }
}
