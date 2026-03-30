package com.ute.foodiedash.application.order.usecase;

import com.ute.foodiedash.application.order.query.OrderSummaryQueryResult;
import com.ute.foodiedash.domain.common.exception.ForbiddenException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.order.model.Order;
import com.ute.foodiedash.domain.order.repository.OrderRepository;
import com.ute.foodiedash.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PrepareOrderUseCase {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderSummaryQueryResult execute(Long merchantId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        boolean canManageRestaurant = userRepository.existsMerchantRestaurant(merchantId, order.getRestaurantId());
        if (!canManageRestaurant) {
            throw new ForbiddenException("You are not allowed to prepare this order");
        }

        order.startPreparing("Order is being prepared by restaurant");

        Order saved = orderRepository.save(order);
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
                saved.getCompleteAt()
        );
    }
}
