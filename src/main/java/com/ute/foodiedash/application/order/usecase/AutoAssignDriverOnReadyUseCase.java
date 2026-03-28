package com.ute.foodiedash.application.order.usecase;

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

    @Value("${driver.dispatch.search-radius-km:10}")
    private double searchRadiusKm;

    @Value("${driver.dispatch.lock-ttl-seconds:30}")
    private long lockTtlSeconds;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void execute(Long orderId) {
        if (orderId == null) {
            return;
        }

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
            if (order.getStatus() == OrderStatus.DELIVERING) {
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
                order.startDelivery("Delivery started (driver already assigned)");
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

            delivery.assignDriver(driverId.get());
            orderDeliveryRepository.save(delivery);

            order.startDelivery("Driver assigned automatically");
            orderRepository.save(order);
            driverAssignmentPendingPort.remove(orderId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            driverAssignmentLockPort.release(orderId);
        }
    }
}
