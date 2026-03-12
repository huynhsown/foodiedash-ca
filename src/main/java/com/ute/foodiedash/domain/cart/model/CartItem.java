package com.ute.foodiedash.domain.cart.model;

import com.ute.foodiedash.domain.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

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

    public void updateQuantity(Integer newQuantity) {
        if (newQuantity == null || newQuantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        this.quantity = newQuantity;
        recalculateTotalPrice();
    }

    public void recalculateTotalPrice() {
        if (unitPrice != null && quantity != null) {
            this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }

    public void increaseQuantity() {
        updateQuantity(this.quantity + 1);
    }

    public boolean canDecrease() {
        return this.quantity > 1;
    }

    public void decreaseQuantity() {
        if (!canDecrease()) {
            throw new IllegalStateException("Cannot decrease quantity below 1");
        }
        updateQuantity(this.quantity - 1);
    }

    public void updateFromMenuItem(String name, String imageUrl,
                                    BigDecimal unitPrice, String notes) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.unitPrice = unitPrice;
        if (notes != null) {
            this.notes = notes;
        }
        recalculateTotalPrice();
    }

    public void updateTotalPrice(BigDecimal unitTotalWithExtras) {
        this.totalPrice = unitTotalWithExtras.multiply(BigDecimal.valueOf(quantity));
    }
}
