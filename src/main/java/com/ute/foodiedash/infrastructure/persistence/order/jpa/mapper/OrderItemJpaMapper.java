package com.ute.foodiedash.infrastructure.persistence.order.jpa.mapper;

import com.ute.foodiedash.domain.order.model.OrderItem;
import com.ute.foodiedash.domain.order.model.OrderItemOption;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderItemJpaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemOptionJpaMapper.class})
public abstract class OrderItemJpaMapper {

    @Autowired
    protected OrderItemOptionJpaMapper orderItemOptionJpaMapper;

    public OrderItem toDomain(OrderItemJpaEntity e) {
        if (e == null) {
            return null;
        }
        List<OrderItemOption> options = e.getOptions() == null
            ? List.of()
            : e.getOptions().stream().map(orderItemOptionJpaMapper::toDomain).toList();

        return OrderItem.reconstruct(
            e.getId(),
            e.getOrder() != null ? e.getOrder().getId() : null,
            e.getMenuItemId(),
            e.getName(),
            e.getImageUrl(),
            e.getQuantity(),
            e.getUnitPrice(),
            e.getTotalPrice(),
            e.getNotes(),
            options,
            e.getCreatedAt(),
            e.getUpdatedAt(),
            e.getCreatedBy(),
            e.getUpdatedBy(),
            e.getDeletedAt(),
            e.getVersion()
        );
    }

    @Mapping(target = "order", ignore = true)
    public abstract OrderItemJpaEntity toJpaEntity(OrderItem domain);

    @AfterMapping
    protected void setItemReferences(@MappingTarget OrderItemJpaEntity e) {
        if (e.getOptions() != null && !e.getOptions().isEmpty()) {
            e.getOptions().forEach(o -> o.setOrderItem(e));
        }
    }
}
