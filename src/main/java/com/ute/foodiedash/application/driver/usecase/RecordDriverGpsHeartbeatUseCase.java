package com.ute.foodiedash.application.driver.usecase;

import com.ute.foodiedash.application.driver.port.DriverBusyStatePort;
import com.ute.foodiedash.application.driver.port.DriverLocationSyncPort;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class RecordDriverGpsHeartbeatUseCase {

    private final DriverLocationSyncPort syncPort;
    private final DriverBusyStatePort driverBusyStatePort;

    public void execute(Long driverId, double latitude, double longitude, Long clientTimestampMillis) {
        if (driverId == null) {
            throw new BadRequestException("Driver id required");
        }
        if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
            throw new BadRequestException("Invalid coordinates");
        }
        Instant at = clientTimestampMillis != null
                ? Instant.ofEpochMilli(clientTimestampMillis)
                : Instant.now();

        syncPort.syncTrackingLocation(driverId, longitude, latitude, at);

        if (!driverBusyStatePort.isOnDelivery(driverId)) {
            syncPort.syncAvailablePoolLocation(driverId, longitude, latitude, at);
        }
    }
}
