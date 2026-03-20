package com.ute.foodiedash.infrastructure.persistence.order.jpa.mapper;

import com.ute.foodiedash.domain.order.model.OrderStatusHistory;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderStatusHistoryJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderStatusHistoryJpaMapper {

    default OrderStatusHistory toDomain(OrderStatusHistoryJpaEntity e) {
        if (e == null) {
            return null;
        }
        return OrderStatusHistory.reconstruct(
            e.getId(),
            e.getOrder() != null ? e.getOrder().getId() : null,
            e.getStatus(),
            e.getNote(),
            e.getCreatedAt(),
            e.getUpdatedAt(),
            e.getCreatedBy(),
            e.getUpdatedBy(),
            e.getDeletedAt(),
            e.getVersion()
        );
    }

    @Mapping(target = "order", ignore = true)
    OrderStatusHistoryJpaEntity toJpaEntity(OrderStatusHistory domain);

    default void updateEntity(@MappingTarget OrderStatusHistoryJpaEntity e, OrderStatusHistory domain) {
        e.setStatus(domain.getStatus());
        e.setNote(domain.getNote());
        e.setDeletedAt(domain.getDeletedAt());
        e.setVersion(domain.getVersion());
    }
}
