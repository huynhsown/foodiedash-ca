package com.ute.foodiedash.application.order.port;

import java.time.Duration;

public interface DriverAssignmentLockPort {

    boolean tryAcquire(Long orderId, Duration ttl);

    void release(Long orderId);
}
