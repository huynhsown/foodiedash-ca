package com.ute.foodiedash.interfaces.rest.order.dto;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDetailResponseDTO {
    private Long orderId;
    private String orderCode;
    private String status;
    private Long subtotal;
    private Long discount;
    private Long deliveryFee;
    private Long total;
    private LocalDateTime placedAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime preparedAt;
    private LocalDateTime cancelledAt;
    private List<ItemDTO> items;
    private List<PromotionDTO> promotions;
    private List<StatusHistoryDTO> statusHistories;
    private PaymentDTO payment;
    private DeliveryDTO delivery;

    @Data
    public static class ItemDTO {
        private Long itemId;
        private Long menuItemId;
        private String name;
        private String imageUrl;
        private Integer quantity;
        private Long unitPrice;
        private Long totalPrice;
        private String notes;
        private List<OptionDTO> options;
    }

    @Data
    public static class OptionDTO {
        private Long optionId;
        private Long sourceOptionId;
        private String optionName;
        private Boolean required;
        private Integer minValue;
        private Integer maxValue;
        private List<OptionValueDTO> values;
    }

    @Data
    public static class OptionValueDTO {
        private Long optionValueId;
        private Long sourceOptionValueId;
        private String optionValueName;
        private Integer quantity;
        private Long extraPrice;
    }

    @Data
    public static class PromotionDTO {
        private Long promotionId;
        private String promotionCode;
        private Long discountAmount;
    }

    @Data
    public static class StatusHistoryDTO {
        private Long statusHistoryId;
        private String status;
        private String note;
        private Instant createdAt;
    }

    @Data
    public static class PaymentDTO {
        private String paymentMethodCode;
        private String paymentStatus;
        private String transactionId;
        private Instant paidAt;
        private Instant refundedAt;
    }

    @Data
    public static class DeliveryDTO {
        private String address;
        private Double lat;
        private Double lng;
        private String receiverName;
        private String receiverPhone;
        private String note;
        private Double distanceKm;
        private Integer etaMinutes;
        private List<CoordinateDTO> geometry;
    }

    @Data
    public static class CoordinateDTO {
        private Double lat;
        private Double lng;
    }
}
