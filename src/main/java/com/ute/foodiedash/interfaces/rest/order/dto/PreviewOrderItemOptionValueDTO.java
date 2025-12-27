package com.ute.foodiedash.interfaces.rest.order.dto;

import lombok.Data;

@Data
public class PreviewOrderItemOptionValueDTO {
    private Long optionValueId;
    private String optionValueName;
    private Integer quantity;
    private Long extraPrice;
    private Long totalExtraPrice;
}

