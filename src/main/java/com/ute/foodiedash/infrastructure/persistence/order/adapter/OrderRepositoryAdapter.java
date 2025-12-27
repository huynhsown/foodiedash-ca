package com.ute.foodiedash.infrastructure.persistence.order.adapter;

import com.ute.foodiedash.domain.order.model.Order;
import com.ute.foodiedash.domain.order.repository.OrderRepository;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.mapper.OrderJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.repository.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {
    private final OrderJpaRepository jpaRepository;
    private final OrderJpaMapper mapper;

    @Override
    public Order save(Order order) {
        OrderJpaEntity jpaEntity = mapper.toJpaEntity(order);
        OrderJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return jpaRepository.findDetailById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Order> findByCode(String code) {
        return jpaRepository.findDetailByCode(code).map(mapper::toDomain);
    }

    @Override
    public List<Order> findSummariesByCustomerId(Long customerId) {
        if (customerId == null) {
            return List.of();
        }

        // Do not join fetch collections here to avoid cartesian explosion and bag-fetch exceptions.
        // We only reconstruct the order core fields; items/promotions/statusHistories are left empty.
        return jpaRepository.findSummariesByCustomerId(customerId).stream()
                .map(this::toDomainSummary)
                .toList();
    }

    private Order toDomainSummary(com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderJpaEntity e) {
        if (e == null) {
            return null;
        }

        return Order.reconstruct(
                e.getId(),
                e.getCode(),
                e.getCustomerId(),
                e.getRestaurantId(),
                e.getStatus(),
                e.getSubtotalAmount(),
                e.getDiscountAmount(),
                e.getDeliveryFee(),
                e.getTotalAmount(),
                e.getPlacedAt(),
                e.getAcceptedAt(),
                e.getPreparedAt(),
                e.getCancelledAt(),
                e.getCancelReason(),
                List.of(),
                List.of(),
                List.of(),
                e.getCreatedAt(),
                e.getUpdatedAt(),
                e.getCreatedBy(),
                e.getUpdatedBy(),
                e.getDeletedAt(),
                e.getVersion()
        );
    }

    @Override
    public void softDeleteById(Long id) {
        jpaRepository.softDeleteById(id);
    }

    @Override
    public void restoreById(Long id) {
        jpaRepository.restoreById(id);
    }
}
