package com.ute.foodiedash.application.driver.port;

import java.time.Instant;

public interface DriverLocationSyncPort {

    void syncTrackingLocation(Long driverId, double longitude, double latitude, Instant at);

    void syncAvailablePoolLocation(Long driverId, double longitude, double latitude, Instant at);
}
