package com.ute.foodiedash.domain.menu.model;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.model.BaseEntity;
import com.ute.foodiedash.domain.menu.enums.MenuStatus;
import java.time.Instant;
import java.time.LocalTime;
import lombok.Getter;

@Getter
public class Menu extends BaseEntity {
    private Long id;
    private Long restaurantId;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private MenuStatus status;

    public static Menu create(Long restaurantId, String name,
                               LocalTime startTime, LocalTime endTime) {
        Menu menu = new Menu();
        menu.restaurantId = restaurantId;
        menu.name = name;
        menu.startTime = startTime;
        menu.endTime = endTime;
        menu.status = MenuStatus.DRAFT;
        return menu;
    }

    public static Menu reconstruct(
            Long id,
            Long restaurantId,
            String name,
            LocalTime startTime,
            LocalTime endTime,
            MenuStatus status,
            Instant createdAt,
            Instant updatedAt,
            String createdBy,
            String updatedBy,
            Instant deletedAt,
            Long version
    ) {
        Menu menu = new Menu();

        menu.id = id;
        menu.restaurantId = restaurantId;
        menu.name = name;
        menu.startTime = startTime;
        menu.endTime = endTime;
        menu.status = status;

        menu.restoreAudit(
                createdAt,
                updatedAt,
                createdBy,
                updatedBy,
                deletedAt,
                version
        );

        return menu;
    }

    // ========== State transition — Submit ==========
    public void submit() {
        if (this.status != MenuStatus.DRAFT) {
            throw new BadRequestException(
                "Menu is not in DRAFT status. Current status: " + this.status);
        }
        this.status = MenuStatus.ACTIVE;
    }

    // ========== Query methods ==========
    public boolean isDraft() {
        return status == MenuStatus.DRAFT;
    }

    public boolean isActive() {
        return status == MenuStatus.ACTIVE;
    }

    // ========== Guard method ==========
    public void ensureActive() {
        if (!isActive()) {
            throw new BadRequestException("Menu is not active");
        }

        LocalTime now = LocalTime.now();

        if (now.isBefore(this.startTime) || now.isAfter(this.endTime)) {
            throw new BadRequestException("Menu is not active");
        }
    }

    public void validate() {
        if (startTime != null && endTime != null && startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
    }
}
