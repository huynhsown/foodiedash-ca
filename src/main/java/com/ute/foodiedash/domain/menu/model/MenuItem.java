package com.ute.foodiedash.domain.menu.model;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.model.BaseEntity;
import com.ute.foodiedash.domain.menu.enums.MenuItemStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;

@Getter
public class MenuItem extends BaseEntity {
    private Long id;
    private Long menuId;
    private Long restaurantId;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private MenuItemStatus status;

    private final List<MenuItemOption> options = new ArrayList<>();

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

    public static MenuItem reconstruct(
            Long id,
            Long menuId,
            Long restaurantId,
            String name,
            String description,
            BigDecimal price,
            String imageUrl,
            MenuItemStatus status,
            List<MenuItemOption> options,
            Instant createdAt,
            Instant updatedAt,
            String createdBy,
            String updatedBy,
            Instant deletedAt,
            Long version
    ) {
        MenuItem item = new MenuItem();

        item.id = id;
        item.menuId = menuId;
        item.restaurantId = restaurantId;
        item.name = name;
        item.description = description;
        item.price = price;
        item.imageUrl = imageUrl;
        item.status = status;

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

    public MenuItemOption addOption(String name, Boolean required, Integer minValue, Integer maxValue) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Option name required");
        }

        boolean exists = this.options.stream()
                .anyMatch(o -> o.getName() != null && o.getName().equalsIgnoreCase(name));

        if (exists) {
            throw new BadRequestException("Option name already exists: " + name);
        }

        MenuItemOption option = MenuItemOption.create(
                this.id,
                name,
                required,
                minValue,
                maxValue
        );
        this.options.add(option);
        return option;
    }

    public MenuItemOptionValue addOptionValue(Long optionId, String name, BigDecimal extraPrice) {
        MenuItemOption option = this.options.stream()
                .filter(o -> optionId != null && optionId.equals(o.getId()))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Menu Item Option not found with id " + optionId));
        return option.addValue(name, extraPrice);
    }

    public Optional<MenuItemOptionValue> getOptionValue(Long optionId, String valueName) {
        if (optionId == null || valueName == null || valueName.isBlank()) {
            return Optional.empty();
        }
        return this.options.stream()
                .filter(o -> optionId.equals(o.getId()))
                .findFirst()
                .flatMap(opt -> opt.getValues().stream()
                        .filter(v -> valueName.equalsIgnoreCase(v.getName()))
                        .findFirst());
    }
}
