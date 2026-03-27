package com.ute.foodiedash.interfaces.rest.order.dto;

import lombok.Data;

import java.util.List;

@Data
public class PreviewOrderItemDTO {
    private Long menuItemId;
    private String name;
    private String imageUrl;
    private Integer quantity;
    private Long unitPrice;
    private Long totalPrice;
    private String notes;
    private List<PreviewOrderItemOptionDTO> options;
}

