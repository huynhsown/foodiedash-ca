package com.ute.foodiedash.domain.restaurant.model;

import com.ute.foodiedash.domain.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
public class RestaurantBusinessHour extends BaseEntity {
    private Long id;
    private Long restaurantId;
    private Integer dayOfWeek;
    private LocalTime openTime;
    private LocalTime closeTime;

    public boolean isOpenAt(DayOfWeek day, LocalTime time) {
        return this.dayOfWeek == day.getValue()
            && !time.isBefore(openTime)
            && !time.isAfter(closeTime);
    }

    public void validate() {
        if (dayOfWeek < 1 || dayOfWeek > 7) {
            throw new IllegalArgumentException("Day of week must be between 1 and 7");
        }
        if (openTime != null && closeTime != null && openTime.isAfter(closeTime)) {
            throw new IllegalArgumentException("Open time must be before close time");
        }
    }
}
