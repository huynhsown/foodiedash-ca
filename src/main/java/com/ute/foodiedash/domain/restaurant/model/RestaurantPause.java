package com.ute.foodiedash.domain.restaurant.model;

import com.ute.foodiedash.domain.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class RestaurantPause extends BaseEntity {
    private Long id;
    private Long restaurantId;
    private String reason;
    private Instant pausedFrom;
    private Instant pausedTo;

    // ========== Check currently paused ==========
    public boolean isCurrentlyPaused() {
        Instant now = Instant.now();
        return pausedFrom != null && pausedTo != null
            && !now.isBefore(pausedFrom)
            && !now.isAfter(pausedTo);
    }

    // ========== Validation ==========
    public void validate() {
        if (pausedFrom != null && pausedTo != null && pausedFrom.isAfter(pausedTo)) {
            throw new IllegalArgumentException("pausedFrom must be before pausedTo");
        }
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Reason must not be blank");
        }
    }
}
