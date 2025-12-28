package com.ute.foodiedash.interfaces.rest.order.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;

/**
 * Response after driver confirms pickup; reuses nested shapes from {@link OrderDetailResponseDTO}.
 */
@Data
public class PickupOrderResponseDTO {
    private Long orderId;
    private String orderCode;
    private String status;
    private Instant pickedUpAt;
    private List<OrderDetailResponseDTO.ItemDTO> items;
    private OrderDetailResponseDTO.DeliveryDTO delivery;
    private OrderDetailResponseDTO.PaymentDTO payment;
}
