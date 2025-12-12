package com.ute.foodiedash.domain.order.model;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.model.BaseEntity;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class OrderItem extends BaseEntity {
    private Long id;
    private Long orderId;
    private Long menuItemId;

    private String name;
    private String imageUrl;

    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    private String notes;

    private final List<OrderItemOption> options = new ArrayList<>();

    public static OrderItem create(
            Long menuItemId,
            String name,
            String imageUrl,
            Integer quantity,
            BigDecimal unitPrice,
            String notes
    ) {
        if (menuItemId == null) {
            throw new BadRequestException("Menu item id required");
        }

        if (name == null || name.isBlank()) {
            throw new BadRequestException("Order item name required");
        }

        if (quantity == null || quantity <= 0) {
            throw new BadRequestException("Quantity must be positive");
        }

        if (unitPrice == null) {
            throw new BadRequestException("Unit price required");
        }

        if (unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Unit price must be positive");
        }

        OrderItem item = new OrderItem();

        item.menuItemId = menuItemId;
        item.name = name;
        item.imageUrl = imageUrl;
        item.quantity = quantity;
        item.unitPrice = unitPrice;
        item.notes = notes;

        item.recalculateTotalPrice();

        return item;
    }

    public static OrderItem reconstruct(
            Long id,
            Long orderId,
            Long menuItemId,
            String name,
            String imageUrl,
            Integer quantity,
            BigDecimal unitPrice,
            BigDecimal totalPrice,
            String notes,
            List<OrderItemOption> options,
            Instant createdAt,
            Instant updatedAt,
            String createdBy,
            String updatedBy,
            Instant deletedAt,
            Long version
    ) {
        OrderItem item = new OrderItem();

        item.id = id;
        item.orderId = orderId;
        item.menuItemId = menuItemId;
        item.name = name;
        item.imageUrl = imageUrl;
        item.quantity = quantity;
        item.unitPrice = unitPrice;
        item.totalPrice = totalPrice;
        item.notes = notes;

        if (options != null && !options.isEmpty()) {
            item.options.addAll(options);
        }

        item.restoreAudit(
                createdAt,
                updatedAt,
                createdBy,
                updatedBy,
                deletedAt,
                version
        );

        return item;
    }

    public void addOption(OrderItemOption option) {
        if (option == null) {
            throw new BadRequestException("Order item option required");
        }
        this.options.add(option);
        recalculateTotalPrice();
    }

    public void removeOptionById(Long optionId) {
        if (optionId == null) {
            return;
        }
        boolean removed = this.options.removeIf(o -> Objects.equals(o.getId(), optionId));
        if (removed) {
            recalculateTotalPrice();
        }
    }

    public void clearOptions() {
        if (this.options.isEmpty()) {
            return;
        }
        this.options.clear();
        recalculateTotalPrice();
    }

    public BigDecimal getTotalExtraPrice() {
        return this.options.stream()
                .flatMap(option -> option.getValues().stream())
                .map(OrderItemOptionValue::getTotalExtraPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void changeQuantity(Integer newQuantity) {
        if (newQuantity == null || newQuantity <= 0) {
            throw new BadRequestException("Quantity must be positive");
        }
        this.quantity = newQuantity;
        recalculateTotalPrice();
    }

    public void recalculateTotalPrice() {
        if (quantity == null || unitPrice == null) {
            return;
        }

        BigDecimal extra = getTotalExtraPrice();
        BigDecimal base = unitPrice.add(extra);

        if (base.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Order item total price cannot be negative");
        }

        this.totalPrice = base.multiply(BigDecimal.valueOf(quantity));
    }
}
