package com.ute.foodiedash.infrastructure.persistence.order.adapter;

import com.ute.foodiedash.application.order.port.OrderValidationPort;
import com.ute.foodiedash.application.order.port.model.OrderValidation;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.repository.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderValidationAdapter implements OrderValidationPort {

    private final OrderJpaRepository jpaRepository;

    @Override
    public Optional<OrderValidation> findForReview(Long orderId) {
        return jpaRepository.findForReview(orderId);
    }
}
