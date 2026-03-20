package com.ute.foodiedash.infrastructure.persistence.order.jpa.mapper;

import com.ute.foodiedash.domain.order.model.OrderItem;
import com.ute.foodiedash.domain.order.model.OrderItemOption;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderItemJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderItemOptionJpaEntity;
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

    public void updateEntity(@MappingTarget OrderItemJpaEntity e, OrderItem domain) {
        e.setMenuItemId(domain.getMenuItemId());
        e.setName(domain.getName());
        e.setImageUrl(domain.getImageUrl());
        e.setQuantity(domain.getQuantity());
        e.setUnitPrice(domain.getUnitPrice());
        e.setTotalPrice(domain.getTotalPrice());
        e.setNotes(domain.getNotes());

        if (domain.getOptions() != null) {
            Set<Long> domainOptionIds = domain.getOptions().stream()
                    .map(OrderItemOption::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            e.getOptions().removeIf(opt -> !domainOptionIds.contains(opt.getId()));

            Map<Long, OrderItemOptionJpaEntity> existingById = e.getOptions().stream()
                    .collect(Collectors.toMap(OrderItemOptionJpaEntity::getId, Function.identity()));

            for (OrderItemOption domainOpt : domain.getOptions()) {
                if (domainOpt.getId() != null && existingById.containsKey(domainOpt.getId())) {
                    orderItemOptionJpaMapper.updateEntity(existingById.get(domainOpt.getId()), domainOpt);
                } else {
                    OrderItemOptionJpaEntity jpaOpt = orderItemOptionJpaMapper.toJpaEntity(domainOpt);
                    jpaOpt.setOrderItem(e);
                    e.getOptions().add(jpaOpt);
                }
            }
        } else {
            e.getOptions().clear();
        }

        e.setDeletedAt(domain.getDeletedAt());
        e.setVersion(domain.getVersion());
    }

    @AfterMapping
    protected void setItemReferences(@MappingTarget OrderItemJpaEntity e) {
        if (e.getOptions() != null && !e.getOptions().isEmpty()) {
            e.getOptions().forEach(o -> o.setOrderItem(e));
        }
    }
}
