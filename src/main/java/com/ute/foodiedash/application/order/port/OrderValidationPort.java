package com.ute.foodiedash.application.order.port;

import com.ute.foodiedash.application.order.port.model.OrderValidation;

import java.util.Optional;

public interface OrderValidationPort {
    Optional<OrderValidation> findForReview(Long orderId);
}
