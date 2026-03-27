package com.ute.foodiedash.interfaces.rest.order.dto;

import lombok.Data;

import java.util.List;

@Data
public class PreviewOrderItemOptionDTO {
    private Long optionId;
    private String optionName;
    private Boolean required;
    private Integer minValue;
    private Integer maxValue;
    private List<PreviewOrderItemOptionValueDTO> values;
}

