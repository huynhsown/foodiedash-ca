package com.ute.foodiedash.infrastructure.persistence.order.adapter;

import com.ute.foodiedash.domain.order.model.Order;
import com.ute.foodiedash.domain.order.repository.OrderRepository;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.mapper.OrderJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.repository.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
    public void softDeleteById(Long id) {
        jpaRepository.softDeleteById(id);
    }

    @Override
    public void restoreById(Long id) {
        jpaRepository.restoreById(id);
    }
}
