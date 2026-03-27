package com.ute.foodiedash.interfaces.rest.order.dto;

import lombok.Data;

@Data
public class PriceBreakdownDTO {
    private Long baseSubtotal;
    private Long extrasSubtotal;
    private Long itemsSubtotal;
    private Long discount;
    private Long deliveryFee;
    private Long total;
}

