package com.ute.foodiedash.domain.cart.model;

import com.ute.foodiedash.domain.common.model.BaseEntity;
import com.ute.foodiedash.domain.cart.enums.CartStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Cart extends BaseEntity {
    private Long id;
    private Long userId;
    private Long restaurantId;
    private CartStatus status;
    private LocalDateTime expiresAt;

    // ========== Factory method ==========
    public static Cart createForUser(Long userId, Long restaurantId) {
        Cart cart = new Cart();
        cart.userId = userId;
        cart.restaurantId = restaurantId;
        cart.status = CartStatus.ACTIVE;
        cart.expiresAt = LocalDateTime.now().plusMinutes(15);
        return cart;
    }

    public void extendExpiry() {
        this.expiresAt = LocalDateTime.now().plusMinutes(15);
    }

    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(LocalDateTime.now());
    }

    public boolean isActive() {
        return status == CartStatus.ACTIVE && !isExpired();
    }
}
