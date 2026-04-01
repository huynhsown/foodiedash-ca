package com.ute.foodiedash.interfaces.rest.notification.dto;

import lombok.Data;

import java.util.List;

@Data
public class ListNotificationsResponseDTO {
    private List<NotificationDTO> notifications;
    private long totalElements;
    private int page;
    private int size;
}

