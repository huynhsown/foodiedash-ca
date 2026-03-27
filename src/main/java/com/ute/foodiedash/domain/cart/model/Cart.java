package com.ute.foodiedash.domain.cart.model;

import com.ute.foodiedash.domain.cart.enums.CartStatus;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class Cart extends BaseEntity {
    private Long id;
    private Long userId;
    private Long restaurantId;
    private CartStatus status;
    private LocalDateTime expiresAt;

    private final List<CartItem> items = new ArrayList<>();

    public static Cart createForUser(Long userId, Long restaurantId) {
        if (userId == null) {
            throw new BadRequestException("User id required");
        }
        if (restaurantId == null) {
            throw new BadRequestException("Restaurant id required");
        }

        Cart cart = new Cart();
        cart.userId = userId;
        cart.restaurantId = restaurantId;
        cart.status = CartStatus.ACTIVE;
        cart.expiresAt = LocalDateTime.now().plusMinutes(15);
        return cart;
    }

    public void extendExpiry() {
        ensureActive();
        this.expiresAt = LocalDateTime.now().plusMinutes(15);
    }

    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(LocalDateTime.now());
    }

    public boolean isActive() {
        return status == CartStatus.ACTIVE && !isExpired();
    }

    public void addItem(CartItem item) {
        ensureActive();
        if (item == null) {
            throw new BadRequestException("Cart item required");
        }
        this.items.add(item);
    }

    public void removeItemById(Long itemId) {
        ensureActive();
        if (itemId == null) {
            return;
        }
        this.items.removeIf(i -> Objects.equals(i.getId(), itemId));
    }

    public void softDeletedItemById(Long itemId) {
        ensureActive();
        if (itemId == null) return;
        CartItem item = this.items.stream()
                .filter(i -> Objects.equals(i.getId(), itemId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Cart item not found"));

        item.markDeleted();
    }

    public void restoreItemById(Long itemId) {
        ensureActive();
        if (itemId == null) return;
        CartItem item = this.items.stream()
                .filter(i -> Objects.equals(i.getId(), itemId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Cart item not found"));

        item.restored();
    }

    public void clearItems() {
        ensureActive();
        this.items.clear();
    }

    public void increaseItemQuantity(Long cartItemId) {
        ensureActive();
        CartItem item = items.stream()
                .filter(i -> i.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Cart item not found"));
        item.increaseQuantity();
    }

    public void decreaseItemQuantity(Long cartItemId) {
        ensureActive();
        CartItem item = items.stream()
                .filter(i -> i.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Cart item not found"));
        item.decreaseQuantity();
    }

    public BigDecimal getTotalAmount() {
        return this.items.stream()
                .map(CartItem::getTotalPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void ensureActive() {
        if (!isActive()) {
            throw new BadRequestException("Cart is not active");
        }
    }
}
