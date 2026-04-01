package com.ute.foodiedash.interfaces.rest.notification.dto;

import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
public class NotificationDTO {
    private Long id;
    private String type;
    private String titleKey;
    private String bodyKey;
    private Map<String, Object> payload;
    private Boolean read;
    private Instant readAt;
    private Instant createdAt;
}

