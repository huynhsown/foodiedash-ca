package com.ute.foodiedash.interfaces.rest.notification.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class MarkNotificationsReadRequestDTO {
    @NotNull
    @NotEmpty
    private List<Long> notificationIds;
}

