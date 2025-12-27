package com.ute.foodiedash.infrastructure.persistence.order.adapter;

import com.ute.foodiedash.domain.order.model.OrderPayment;
import com.ute.foodiedash.domain.order.repository.OrderPaymentRepository;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderPaymentJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.mapper.OrderPaymentJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.repository.OrderPaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderPaymentRepositoryAdapter implements OrderPaymentRepository {
    private final OrderPaymentJpaRepository jpaRepository;
    private final OrderPaymentJpaMapper mapper;

    @Override
    public OrderPayment save(OrderPayment orderPayment) {
        OrderPaymentJpaEntity jpaEntity = mapper.toJpaEntity(orderPayment);
        OrderPaymentJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<OrderPayment> findByOrderId(Long orderId) {
        return jpaRepository.findByOrderId(orderId).map(mapper::toDomain);
    }
}
