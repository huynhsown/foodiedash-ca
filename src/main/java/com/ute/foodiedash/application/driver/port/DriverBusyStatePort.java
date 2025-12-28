package com.ute.foodiedash.application.driver.port;

import java.time.Duration;

public interface DriverBusyStatePort {

    void markOnDelivery(Long driverId, Long orderId, Duration ttl);

    void clear(Long driverId);

    boolean isOnDelivery(Long driverId);
}
