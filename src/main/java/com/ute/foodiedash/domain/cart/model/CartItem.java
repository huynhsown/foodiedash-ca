package com.ute.foodiedash.domain.cart.model;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class CartItem extends BaseEntity {
    private Long id;
    private Long cartId;
    private Long menuItemId;
    private String name;
    private String imageUrl;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private String notes;

    private final List<CartItemOption> options = new ArrayList<>();

    public static CartItem create(
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
            throw new BadRequestException("Cart item name required");
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

        CartItem item = new CartItem();

        item.menuItemId = menuItemId;
        item.name = name;
        item.imageUrl = imageUrl;
        item.quantity = quantity;
        item.unitPrice = unitPrice;
        item.notes = notes;

        item.recalculateTotalPrice();

        return item;
    }

    public static CartItem reconstruct(
            Long id,
            Long cartId,
            Long menuItemId,
            String name,
            String imageUrl,
            Integer quantity,
            BigDecimal unitPrice,
            BigDecimal totalPrice,
            String notes,
            List<CartItemOption> options,
            Instant createdAt,
            Instant updatedAt,
            String createdBy,
            String updatedBy,
            Instant deletedAt,
            Long version
    ) {
        CartItem item = new CartItem();

        item.id = id;
        item.cartId = cartId;
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

    public void updateQuantity(Integer newQuantity) {
        ensureActive();
        if (newQuantity == null || newQuantity <= 0) {
            throw new BadRequestException("Quantity must be positive");
        }
        this.quantity = newQuantity;
        recalculateTotalPrice();
    }

    public void increaseQuantity() {
        ensureActive();
        updateQuantity(this.quantity + 1);
    }

    public boolean canDecrease() {
        return this.quantity > 1;
    }

    public void decreaseQuantity() {
        ensureActive();
        if (!canDecrease()) {
            this.markDeleted();
            return;
        }
        updateQuantity(this.quantity - 1);
    }

    public BigDecimal getTotalExtraPrice() {
        return this.options.stream()
                .flatMap(option -> option.getValues().stream())
                .map(CartItemOptionValue::getTotalExtraPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void changeQuantity(Integer newQuantity) {
        ensureActive();
        updateQuantity(newQuantity);
    }

    public void recalculateTotalPrice() {
        if (quantity == null || unitPrice == null) {
            return;
        }

        BigDecimal extra = getTotalExtraPrice();
        BigDecimal base = unitPrice.add(extra);

        if (base.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Cart item total price cannot be negative");
        }

        this.totalPrice = base.multiply(BigDecimal.valueOf(quantity));
    }

    public void updateFromMenuItem(String name, String imageUrl,
                                   BigDecimal unitPrice, String notes) {
        ensureActive();
        this.name = name;
        this.imageUrl = imageUrl;
        this.unitPrice = unitPrice;
        if (notes != null) {
            this.notes = notes;
        }
        recalculateTotalPrice();
    }

    public void updateTotalPrice(BigDecimal unitTotalWithExtras) {
        ensureActive();
        this.totalPrice = unitTotalWithExtras.multiply(BigDecimal.valueOf(quantity));
    }

    public void addOption(CartItemOption option) {
        ensureActive();
        if (option == null) {
            throw new BadRequestException("Cart item option required");
        }
        this.options.add(option);
        recalculateTotalPrice();
    }

    public void removeOptionById(Long optionId) {
        ensureActive();
        if (optionId == null) {
            return;
        }
        boolean removed = this.options.removeIf(o -> Objects.equals(o.getId(), optionId));
        if (removed) {
            recalculateTotalPrice();
        }
    }

    public void clearOptions() {
        ensureActive();
        if (this.options.isEmpty()) {
            return;
        }
        this.options.clear();
        recalculateTotalPrice();
    }

    public boolean isActive() {
        return !isDeleted();
    }

    private void ensureActive() {
        if (!isActive()) {
            throw new BadRequestException("Cart item is not active");
        }
    }
}
