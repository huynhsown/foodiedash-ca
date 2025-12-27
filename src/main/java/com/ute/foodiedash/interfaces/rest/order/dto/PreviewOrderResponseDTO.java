package com.ute.foodiedash.interfaces.rest.order.dto;

import com.ute.foodiedash.interfaces.rest.paymentmethod.dto.PaymentMethodResponseDTO;
import lombok.Data;

import java.util.List;

@Data
public class PreviewOrderResponseDTO {
    private List<PreviewOrderItemDTO> items;
    private PromotionPreviewDTO promotion;
    private PriceBreakdownDTO priceBreakdown;
    private Long total;
    private Long distanceMeters;
    private Integer etaInMinutes;
    private boolean canCheckout;
    private List<PaymentMethodResponseDTO> paymentMethods;
}

