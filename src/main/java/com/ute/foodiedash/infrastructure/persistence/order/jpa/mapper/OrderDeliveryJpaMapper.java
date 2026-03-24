package com.ute.foodiedash.infrastructure.persistence.order.jpa.mapper;

import com.ute.foodiedash.domain.order.model.OrderDelivery;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderDeliveryJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderDeliveryJpaMapper {

    default OrderDelivery toDomain(OrderDeliveryJpaEntity e) {
        if (e == null) {
            return null;
        }
        return OrderDelivery.reconstruct(
            e.getId(),
            e.getOrderId(),
            e.getDriverId(),
            e.getAddress(),
            e.getLat(),
            e.getLng(),
            e.getReceiverName(),
            e.getReceiverPhone(),
            e.getNote(),
            e.getDistanceKm(),
            e.getEtaMinutes(),
            e.getGeometry(),
            e.getPickedUpAt(),
            e.getDeliveredAt()
        );
    }

    OrderDeliveryJpaEntity toJpaEntity(OrderDelivery domain);
}
