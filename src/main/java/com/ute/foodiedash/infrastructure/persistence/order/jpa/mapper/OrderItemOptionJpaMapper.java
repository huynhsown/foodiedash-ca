package com.ute.foodiedash.infrastructure.persistence.order.jpa.mapper;

import com.ute.foodiedash.domain.order.model.OrderItemOption;
import com.ute.foodiedash.domain.order.model.OrderItemOptionValue;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderItemOptionJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderItemOptionValueJpaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public void updateEntity(@MappingTarget OrderItemOptionJpaEntity e, OrderItemOption domain) {
        e.setOptionId(domain.getOptionId());
        e.setOptionName(domain.getOptionName());
        e.setRequired(domain.getRequired());
        e.setMinValue(domain.getMinValue());
        e.setMaxValue(domain.getMaxValue());
        mergeValues(e, domain);
        e.setDeletedAt(domain.getDeletedAt());
        e.setVersion(domain.getVersion());
    }

    private void mergeValues(OrderItemOptionJpaEntity e, OrderItemOption domain) {
        if (domain.getValues() == null) {
            e.getValues().clear();
            return;
        }

        Set<Long> domainValueIds = domain.getValues().stream()
                .map(OrderItemOptionValue::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        e.getValues().removeIf(v -> !domainValueIds.contains(v.getId()));

        Map<Long, OrderItemOptionValueJpaEntity> existingById = e.getValues().stream()
                .collect(Collectors.toMap(OrderItemOptionValueJpaEntity::getId, Function.identity()));

        for (OrderItemOptionValue domainVal : domain.getValues()) {
            if (domainVal.getId() != null && existingById.containsKey(domainVal.getId())) {
                orderItemOptionValueJpaMapper.updateEntity(existingById.get(domainVal.getId()), domainVal);
            } else {
                OrderItemOptionValueJpaEntity jpaVal = orderItemOptionValueJpaMapper.toJpaEntity(domainVal);
                jpaVal.setOrderItemOption(e);
                e.getValues().add(jpaVal);
            }
        }
    }

    @AfterMapping
    protected void setOptionReferences(@MappingTarget OrderItemOptionJpaEntity e) {
        if (e.getValues() != null && !e.getValues().isEmpty()) {
            e.getValues().forEach(v -> v.setOrderItemOption(e));
        }
    }
}
