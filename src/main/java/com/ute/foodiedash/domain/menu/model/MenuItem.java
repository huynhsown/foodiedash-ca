package com.ute.foodiedash.domain.menu.model;

import com.ute.foodiedash.domain.common.model.BaseEntity;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.menu.enums.MenuItemStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MenuItem extends BaseEntity {
    private Long id;
    private Long menuId;
    private Long restaurantId;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private MenuItemStatus status;

    // ========== Factory method ==========
    public static MenuItem create(Long menuId, Long restaurantId, String name,
                                   String description, BigDecimal price) {
        MenuItem item = new MenuItem();
        item.menuId = menuId;
        item.restaurantId = restaurantId;
        item.name = name;
        item.description = description;
        item.price = price;
        item.status = MenuItemStatus.ACTIVE;
        return item;
    }

    // ========== Guard — ensure active ==========
    public void ensureActive() {
        if (status != MenuItemStatus.ACTIVE) {
            throw new BadRequestException("Menu item is not active");
        }
    }

    public boolean isActive() {
        return status == MenuItemStatus.ACTIVE;
    }

    // ========== Ensure belongs to restaurant ==========
    public void ensureBelongsToRestaurant(Long restaurantId) {
        if (!this.restaurantId.equals(restaurantId)) {
            throw new BadRequestException("Menu item does not belong to restaurant");
        }
    }

    // ========== Assign image ==========
    public void assignImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
