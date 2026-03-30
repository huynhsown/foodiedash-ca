package com.ute.foodiedash.application.order.port;

import java.util.Optional;

/**
 * Finds and reserves the nearest available driver using a Redis GEO set (see {@code available_drivers}).
 * Implementations MUST remove the chosen member from the geo set so the driver is not assigned twice.
 */
public interface NearestAvailableDriverPort {

    Optional<Long> tryReserveNearestDriver(double longitude, double latitude, double radiusKm);
}
