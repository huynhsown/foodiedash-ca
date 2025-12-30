package com.ute.foodiedash.application.order.usecase;

import com.ute.foodiedash.application.driver.port.DriverBusyStatePort;
import com.ute.foodiedash.application.order.port.DriverAssignmentLockPort;
import com.ute.foodiedash.application.order.port.DriverAssignmentPendingPort;
import com.ute.foodiedash.application.order.port.NearestAvailableDriverPort;
import com.ute.foodiedash.domain.order.enums.OrderStatus;
import com.ute.foodiedash.domain.order.model.Order;
import com.ute.foodiedash.domain.order.model.OrderDelivery;
import com.ute.foodiedash.domain.order.repository.OrderDeliveryRepository;
import com.ute.foodiedash.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Duration;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AutoAssignDriverOnReadyUseCase {

    private final OrderRepository orderRepository;
    private final OrderDeliveryRepository orderDeliveryRepository;
    private final NearestAvailableDriverPort nearestAvailableDriverPort;
    private final DriverAssignmentPendingPort driverAssignmentPendingPort;
    private final DriverAssignmentLockPort driverAssignmentLockPort;
    private final DriverBusyStatePort driverBusyStatePort;

    @Value("${driver.dispatch.search-radius-km:10}")
    private double searchRadiusKm;

    @Value("${driver.dispatch.lock-ttl-seconds:30}")
    private long lockTtlSeconds;

    @Value("${driver.dispatch.driver-busy-ttl-hours:8}")
    private long driverBusyTtlHours;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void execute(Long orderId) {
        if (orderId == null) {
            return;
        }

        Duration busyTtl = Duration.ofHours(driverBusyTtlHours);

        boolean locked = driverAssignmentLockPort.tryAcquire(orderId, Duration.ofSeconds(lockTtlSeconds));
        if (!locked) {
            return;
        }

        try {
            Optional<Order> orderOpt = orderRepository.findById(orderId);
            if (orderOpt.isEmpty()) {
                driverAssignmentPendingPort.remove(orderId);
                return;
            }

            Order order = orderOpt.get();
            if (order.getStatus() == OrderStatus.DELIVERING || order.getStatus() == OrderStatus.AWAITING_PICKUP) {
                driverAssignmentPendingPort.remove(orderId);
                return;
            }
            if (order.getStatus() != OrderStatus.READY) {
                return;
            }

            Optional<OrderDelivery> deliveryOpt = orderDeliveryRepository.findByOrderId(orderId);
            if (deliveryOpt.isEmpty()) {
                driverAssignmentPendingPort.enqueue(orderId);
                return;
            }

            OrderDelivery delivery = deliveryOpt.get();

            if (delivery.getDriverId() != null) {
                driverBusyStatePort.markOnDelivery(delivery.getDriverId(), orderId, busyTtl);
                registerClearBusyOnRollback(delivery.getDriverId());
                order.markAsAwaitingPickup("Driver already assigned — awaiting pickup");
                orderRepository.save(order);
                driverAssignmentPendingPort.remove(orderId);
                return;
            }

            if (delivery.getLat() == null || delivery.getLng() == null) {
                driverAssignmentPendingPort.enqueue(orderId);
                return;
            }

            double lng = delivery.getLng().doubleValue();
            double lat = delivery.getLat().doubleValue();

            Optional<Long> driverId = nearestAvailableDriverPort.tryReserveNearestDriver(lng, lat, searchRadiusKm);
            if (driverId.isEmpty()) {
                driverAssignmentPendingPort.enqueue(orderId);
                return;
            }

            Long assignedDriverId = driverId.get();
            driverBusyStatePort.markOnDelivery(assignedDriverId, orderId, busyTtl);
            registerClearBusyOnRollback(assignedDriverId);

            delivery.assignDriver(assignedDriverId);
            orderDeliveryRepository.save(delivery);

            order.markAsAwaitingPickup("Driver assigned automatically");
            orderRepository.save(order);
            driverAssignmentPendingPort.remove(orderId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            driverAssignmentLockPort.release(orderId);
        }
    }

    private void registerClearBusyOnRollback(Long driverId) {
        if (driverId == null || !TransactionSynchronizationManager.isSynchronizationActive()) {
            return;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                    driverBusyStatePort.clear(driverId);
                }
            }
        });
    }
}
