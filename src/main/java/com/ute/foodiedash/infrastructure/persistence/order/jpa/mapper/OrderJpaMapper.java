package com.ute.foodiedash.infrastructure.persistence.order.jpa.mapper;

import com.ute.foodiedash.domain.order.model.Order;
import com.ute.foodiedash.domain.order.model.OrderItem;
import com.ute.foodiedash.domain.order.model.OrderPromotion;
import com.ute.foodiedash.domain.order.model.OrderStatusHistory;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderItemJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderPromotionJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderStatusHistoryJpaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {
        OrderItemJpaMapper.class,
        OrderPromotionJpaMapper.class,
        OrderStatusHistoryJpaMapper.class
})
public abstract class OrderJpaMapper {

    @Autowired
    protected OrderItemJpaMapper orderItemJpaMapper;
    @Autowired
    protected OrderPromotionJpaMapper orderPromotionJpaMapper;
    @Autowired
    protected OrderStatusHistoryJpaMapper orderStatusHistoryJpaMapper;

    public Order toDomain(OrderJpaEntity e) {
        if (e == null) {
            return null;
        }
        List<OrderItem> items = e.getItems() == null
            ? List.of()
            : e.getItems().stream().map(orderItemJpaMapper::toDomain).toList();
        List<OrderPromotion> promotions = e.getPromotions() == null
            ? List.of()
            : e.getPromotions().stream().map(orderPromotionJpaMapper::toDomain).toList();
        List<OrderStatusHistory> statusHistories = e.getStatusHistories() == null
            ? List.of()
            : e.getStatusHistories().stream().map(orderStatusHistoryJpaMapper::toDomain).toList();

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
            e.getCompleteAt(),
            e.getCancelReason(),
            items,
            promotions,
            statusHistories,
            e.getCreatedAt(),
            e.getUpdatedAt(),
            e.getCreatedBy(),
            e.getUpdatedBy(),
            e.getDeletedAt(),
            e.getVersion()
        );
    }

    public abstract OrderJpaEntity toJpaEntity(Order domain);

    public void updateEntity(@MappingTarget OrderJpaEntity e, Order domain) {
        e.setId(domain.getId());
        e.setCode(domain.getCode());
        e.setCustomerId(domain.getCustomerId());
        e.setRestaurantId(domain.getRestaurantId());
        e.setStatus(domain.getStatus());
        e.setSubtotalAmount(domain.getSubtotalAmount());
        e.setDiscountAmount(domain.getDiscountAmount());
        e.setDeliveryFee(domain.getDeliveryFee());
        e.setTotalAmount(domain.getTotalAmount());
        e.setPlacedAt(domain.getPlacedAt());
        e.setAcceptedAt(domain.getAcceptedAt());
        e.setPreparedAt(domain.getPreparedAt());
        e.setCancelledAt(domain.getCancelledAt());
        e.setCompleteAt(domain.getCompleteAt());
        e.setCancelReason(domain.getCancelReason());

        if (domain.getItems() != null) {
            Set<Long> domainItemIds = domain.getItems().stream()
                    .map(OrderItem::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            e.getItems().removeIf(item -> !domainItemIds.contains(item.getId()));

            Map<Long, OrderItemJpaEntity> existingById = e.getItems().stream()
                    .collect(Collectors.toMap(OrderItemJpaEntity::getId, Function.identity()));

            for (OrderItem domainItem : domain.getItems()) {
                if (domainItem.getId() != null && existingById.containsKey(domainItem.getId())) {
                    orderItemJpaMapper.updateEntity(existingById.get(domainItem.getId()), domainItem);
                } else {
                    OrderItemJpaEntity jpaItem = orderItemJpaMapper.toJpaEntity(domainItem);
                    jpaItem.setOrder(e);
                    e.getItems().add(jpaItem);
                }
            }
        } else {
            e.getItems().clear();
        }

        if (domain.getPromotions() != null) {
            Set<Long> domainPromotionIds = domain.getPromotions().stream()
                    .map(OrderPromotion::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            e.getPromotions().removeIf(promo -> !domainPromotionIds.contains(promo.getId()));

            Map<Long, OrderPromotionJpaEntity> existingById = e.getPromotions().stream()
                    .collect(Collectors.toMap(OrderPromotionJpaEntity::getId, Function.identity()));

            for (OrderPromotion domainPromo : domain.getPromotions()) {
                if (domainPromo.getId() != null && existingById.containsKey(domainPromo.getId())) {
                    orderPromotionJpaMapper.updateEntity(existingById.get(domainPromo.getId()), domainPromo);
                } else {
                    OrderPromotionJpaEntity jpaPromo = orderPromotionJpaMapper.toJpaEntity(domainPromo);
                    jpaPromo.setOrder(e);
                    e.getPromotions().add(jpaPromo);
                }
            }
        } else {
            e.getPromotions().clear();
        }

        if (domain.getStatusHistories() != null) {
            Set<Long> domainHistoryIds = domain.getStatusHistories().stream()
                    .map(OrderStatusHistory::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            e.getStatusHistories().removeIf(hist -> !domainHistoryIds.contains(hist.getId()));

            Map<Long, OrderStatusHistoryJpaEntity> existingById = e.getStatusHistories().stream()
                    .collect(Collectors.toMap(OrderStatusHistoryJpaEntity::getId, Function.identity()));

            for (OrderStatusHistory domainHist : domain.getStatusHistories()) {
                if (domainHist.getId() != null && existingById.containsKey(domainHist.getId())) {
                    orderStatusHistoryJpaMapper.updateEntity(existingById.get(domainHist.getId()), domainHist);
                } else {
                    OrderStatusHistoryJpaEntity jpaHist = orderStatusHistoryJpaMapper.toJpaEntity(domainHist);
                    jpaHist.setOrder(e);
                    e.getStatusHistories().add(jpaHist);
                }
            }
        } else {
            e.getStatusHistories().clear();
        }

        e.setDeletedAt(domain.getDeletedAt());
        e.setVersion(domain.getVersion());
    }

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
