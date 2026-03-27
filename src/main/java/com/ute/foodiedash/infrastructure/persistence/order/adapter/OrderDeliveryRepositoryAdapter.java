package com.ute.foodiedash.infrastructure.persistence.order.adapter;

import com.ute.foodiedash.domain.order.model.OrderDelivery;
import com.ute.foodiedash.domain.order.repository.OrderDeliveryRepository;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderDeliveryJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.mapper.OrderDeliveryJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.repository.OrderDeliveryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderDeliveryRepositoryAdapter implements OrderDeliveryRepository {
    private final OrderDeliveryJpaRepository jpaRepository;
    private final OrderDeliveryJpaMapper mapper;

    @Override
    public OrderDelivery save(OrderDelivery orderDelivery) {
        OrderDeliveryJpaEntity jpaEntity = mapper.toJpaEntity(orderDelivery);
        OrderDeliveryJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }
}
