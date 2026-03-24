package com.ute.foodiedash.interfaces.rest.review.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderInfoDTO {
    private Long orderId;
    private List<OrderItemDTO> items;
}
