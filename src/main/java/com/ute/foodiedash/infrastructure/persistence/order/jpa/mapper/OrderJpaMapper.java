package com.ute.foodiedash.infrastructure.persistence.order.jpa.mapper;

import com.ute.foodiedash.domain.order.model.Order;
import com.ute.foodiedash.domain.order.model.OrderItem;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderJpaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemJpaMapper.class})
public abstract class OrderJpaMapper {

    @Autowired
    protected OrderItemJpaMapper orderItemJpaMapper;

    public Order toDomain(OrderJpaEntity e) {
        if (e == null) {
            return null;
        }
        List<OrderItem> items = e.getItems() == null
            ? List.of()
            : e.getItems().stream().map(orderItemJpaMapper::toDomain).toList();

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
            items,
            e.getCreatedAt(),
            e.getUpdatedAt(),
            e.getCreatedBy(),
            e.getUpdatedBy(),
            e.getDeletedAt(),
            e.getVersion()
        );
    }

    public abstract OrderJpaEntity toJpaEntity(Order domain);

    @AfterMapping
    protected void setOrderReferences(@MappingTarget OrderJpaEntity e) {
        if (e.getItems() != null && !e.getItems().isEmpty()) {
            e.getItems().forEach(i -> i.setOrder(e));
        }
        if (e.getPromotions() != null && !e.getPromotions().isEmpty()) {
            e.getPromotions().forEach(p -> p.setOrder(e));
        }
        if (e.getStatusHistories() != null && !e.getStatusHistories().isEmpty()) {
            e.getStatusHistories().forEach(h -> h.setOrder(e));
        }
    }

}
