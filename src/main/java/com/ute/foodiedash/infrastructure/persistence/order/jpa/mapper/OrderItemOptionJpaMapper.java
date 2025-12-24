package com.ute.foodiedash.infrastructure.persistence.order.jpa.mapper;

import com.ute.foodiedash.domain.order.model.OrderItemOption;
import com.ute.foodiedash.domain.order.model.OrderItemOptionValue;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderItemOptionJpaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemOptionValueJpaMapper.class})
public abstract class OrderItemOptionJpaMapper {

    @Autowired
    protected OrderItemOptionValueJpaMapper orderItemOptionValueJpaMapper;

    public OrderItemOption toDomain(OrderItemOptionJpaEntity e) {
        if (e == null) {
            return null;
        }
        List<OrderItemOptionValue> values = e.getValues() == null
            ? List.of()
            : e.getValues().stream().map(orderItemOptionValueJpaMapper::toDomain).toList();

        return OrderItemOption.reconstruct(
            e.getId(),
            e.getOrderItem() != null ? e.getOrderItem().getId() : null,
            e.getOptionId(),
            e.getOptionName(),
            e.getRequired(),
            e.getMinValue(),
            e.getMaxValue(),
            values,
            e.getCreatedAt(),
            e.getUpdatedAt(),
            e.getCreatedBy(),
            e.getUpdatedBy(),
            e.getDeletedAt(),
            e.getVersion()
        );
    }

    @Mapping(target = "orderItem", ignore = true)
    public abstract OrderItemOptionJpaEntity toJpaEntity(OrderItemOption domain);

    @AfterMapping
    protected void setOptionReferences(@MappingTarget OrderItemOptionJpaEntity e) {
        if (e.getValues() != null && !e.getValues().isEmpty()) {
            e.getValues().forEach(v -> v.setOrderItemOption(e));
        }
    }
}
