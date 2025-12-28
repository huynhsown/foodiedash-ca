package com.ute.foodiedash.application.driver.port;

import java.time.Instant;

public interface DriverLocationSyncPort {
    void syncAvailableDriverLocation(Long driverId, double longitude, double latitude, Instant at);
}
