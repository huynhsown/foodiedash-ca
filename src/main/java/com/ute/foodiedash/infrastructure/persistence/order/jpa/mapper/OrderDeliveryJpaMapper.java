package com.ute.foodiedash.infrastructure.persistence.order.jpa.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ute.foodiedash.domain.order.model.Coordinate;
import com.ute.foodiedash.domain.order.model.OrderDelivery;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderDeliveryJpaEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderDeliveryJpaMapper {
    ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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
            toGeometry(e.getGeometry()),
            e.getPickedUpAt(),
            e.getDeliveredAt()
        );
    }

    default OrderDeliveryJpaEntity toJpaEntity(OrderDelivery domain) {
        if (domain == null) {
            return null;
        }
        OrderDeliveryJpaEntity e = new OrderDeliveryJpaEntity();
        e.setId(domain.getId());
        e.setOrderId(domain.getOrderId());
        e.setDriverId(domain.getDriverId());
        e.setAddress(domain.getAddress());
        e.setLat(domain.getLat());
        e.setLng(domain.getLng());
        e.setReceiverName(domain.getReceiverName());
        e.setReceiverPhone(domain.getReceiverPhone());
        e.setNote(domain.getNote());
        e.setDistanceKm(domain.getDistanceKm());
        e.setEtaMinutes(domain.getEtaMinutes());
        e.setGeometry(toGeometryJson(domain.getGeometry()));
        e.setPickedUpAt(domain.getPickedUpAt());
        e.setDeliveredAt(domain.getDeliveredAt());
        e.setCreatedAt(domain.getCreatedAt());
        e.setUpdatedAt(domain.getUpdatedAt());
        e.setCreatedBy(domain.getCreatedBy());
        e.setUpdatedBy(domain.getUpdatedBy());
        e.setDeletedAt(domain.getDeletedAt());
        e.setVersion(domain.getVersion());
        return e;
    }

    default List<Coordinate> toGeometry(String geometryJson) {
        if (geometryJson == null || geometryJson.isBlank()) {
            return List.of();
        }
        try {
            return OBJECT_MAPPER.readValue(geometryJson, new TypeReference<List<Coordinate>>() {});
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Cannot parse order delivery geometry", ex);
        }
    }

    default String toGeometryJson(List<Coordinate> geometry) {
        if (geometry == null || geometry.isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(geometry);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Cannot serialize order delivery geometry", ex);
        }
    }
}
