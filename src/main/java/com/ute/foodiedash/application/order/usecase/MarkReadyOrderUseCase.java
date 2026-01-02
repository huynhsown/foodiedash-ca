package com.ute.foodiedash.application.order.usecase;

import com.ute.foodiedash.application.common.port.DomainEventPublisher;
import com.ute.foodiedash.application.order.query.OrderSummaryQueryResult;
import com.ute.foodiedash.application.order.port.OrderCustomerNotificationPort;
import com.ute.foodiedash.domain.notification.enums.NotificationType;
import com.ute.foodiedash.domain.common.exception.ForbiddenException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.order.model.Order;
import com.ute.foodiedash.application.order.event.OrderMarkedReadyEvent;
import com.ute.foodiedash.domain.order.repository.OrderRepository;
import com.ute.foodiedash.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MarkReadyOrderUseCase {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final DomainEventPublisher eventPublisher;

    private final EntityManager entityManager;
    private final OrderCustomerNotificationPort orderCustomerNotificationPort;


    @Transactional
    public OrderSummaryQueryResult execute(Long merchantId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        boolean canManageRestaurant = userRepository.existsMerchantRestaurant(merchantId, order.getRestaurantId());
        if (!canManageRestaurant) {
            throw new ForbiddenException("You are not allowed to mark this order as ready");
        }

        order.markReady("Order is ready for delivery");

        Order saved = orderRepository.save(order);
        orderCustomerNotificationPort.notifyForOrderStatus(saved, NotificationType.ORDER_READY);
        eventPublisher.publish(new OrderMarkedReadyEvent(this, saved.getId()));
        return new OrderSummaryQueryResult(
                saved.getId(),
                saved.getCode(),
                saved.getStatus() != null ? saved.getStatus().name() : null,
                saved.getSubtotalAmount(),
                saved.getDiscountAmount(),
                saved.getDeliveryFee(),
                saved.getTotalAmount(),
                saved.getPlacedAt(),
                saved.getAcceptedAt(),
                saved.getPreparedAt(),
                saved.getCancelledAt(),
                saved.getCompleteAt(),
                null,
                null,
                null
        );
    }
}
