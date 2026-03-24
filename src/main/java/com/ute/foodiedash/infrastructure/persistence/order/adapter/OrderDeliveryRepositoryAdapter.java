package com.ute.foodiedash.infrastructure.persistence.order.adapter;

import com.ute.foodiedash.domain.order.model.OrderDelivery;
import com.ute.foodiedash.domain.order.repository.OrderDeliveryRepository;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderDeliveryJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.mapper.OrderDeliveryJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.repository.OrderDeliveryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderDeliveryRepositoryAdapter implements OrderDeliveryRepository {
    private final OrderDeliveryJpaRepository jpaRepository;
    private final OrderDeliveryJpaMapper mapper;

    @Override
    public OrderDelivery save(OrderDelivery orderDelivery) {
        OrderDeliveryJpaEntity jpaEntity;
        if (orderDelivery.getId() == null) {
            jpaEntity = mapper.toJpaEntity(orderDelivery);
        } else {
            jpaEntity = jpaRepository.findById(orderDelivery.getId())
                    .orElseThrow();
            mapper.updateEntity(jpaEntity, orderDelivery);
        }
        OrderDeliveryJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<OrderDelivery> findByOrderId(Long orderId) {
        return jpaRepository.findByOrderId(orderId).map(mapper::toDomain);
    }
}
