package com.ute.foodiedash.application.order.port;

import java.util.Set;

/**
 * Tracks order ids that still need a driver assignment (e.g. no driver in radius).
 */
public interface DriverAssignmentPendingPort {

    void enqueue(Long orderId);

    void remove(Long orderId);

    Set<Long> snapshotPendingOrderIds();
}
