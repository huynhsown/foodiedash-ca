package com.ute.foodiedash.domain.order.model;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.model.BaseEntity;
import com.ute.foodiedash.domain.order.enums.OrderStatus;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

@Getter
public class Order extends BaseEntity {
    private Long id;
    private String code;
    private Long customerId;
    private Long restaurantId;
    private OrderStatus status;

    private BigDecimal subtotalAmount;
    private BigDecimal discountAmount;
    private BigDecimal deliveryFee;
    private BigDecimal totalAmount;

    private LocalDateTime placedAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime preparedAt;
    private LocalDateTime cancelledAt;
    private String cancelReason;

    private final List<OrderItem> items = new ArrayList<>();
    private final List<OrderPromotion> promotions = new ArrayList<>();
    private final List<OrderStatusHistory> statusHistories = new ArrayList<>();

    private static final EnumSet<OrderStatus> FINAL_STATUSES =
            EnumSet.of(OrderStatus.COMPLETED, OrderStatus.CANCELLED);

    public static Order create(
            String code,
            Long customerId,
            Long restaurantId,
            BigDecimal deliveryFee
    ) {
        if (customerId == null) {
            throw new BadRequestException("Customer id required");
        }
        if (restaurantId == null) {
            throw new BadRequestException("Restaurant id required");
        }

        if (deliveryFee == null) {
            deliveryFee = BigDecimal.ZERO;
        }
        if (deliveryFee.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Delivery fee must be positive");
        }

        Order order = new Order();

        order.code = code;
        order.customerId = customerId;
        order.restaurantId = restaurantId;
        order.status = OrderStatus.PENDING;
        order.placedAt = LocalDateTime.now();

        order.subtotalAmount = BigDecimal.ZERO;
        order.discountAmount = BigDecimal.ZERO;
        order.deliveryFee = deliveryFee;
        order.totalAmount = BigDecimal.ZERO;

        order.recalculateTotals();

        return order;
    }

    public static Order reconstruct(
            Long id,
            String code,
            Long customerId,
            Long restaurantId,
            OrderStatus status,
            BigDecimal subtotalAmount,
            BigDecimal discountAmount,
            BigDecimal deliveryFee,
            BigDecimal totalAmount,
            LocalDateTime placedAt,
            LocalDateTime acceptedAt,
            LocalDateTime preparedAt,
            LocalDateTime cancelledAt,
            String cancelReason,
            List<OrderItem> items,
            List<OrderPromotion> promotions,
            List<OrderStatusHistory> statusHistories,
            Instant createdAt,
            Instant updatedAt,
            String createdBy,
            String updatedBy,
            Instant deletedAt,
            Long version
    ) {
        Order order = new Order();

        order.id = id;
        order.code = code;
        order.customerId = customerId;
        order.restaurantId = restaurantId;
        order.status = status;

        order.subtotalAmount = subtotalAmount;
        order.discountAmount = discountAmount;
        order.deliveryFee = deliveryFee;
        order.totalAmount = totalAmount;

        order.placedAt = placedAt;
        order.acceptedAt = acceptedAt;
        order.preparedAt = preparedAt;
        order.cancelledAt = cancelledAt;
        order.cancelReason = cancelReason;

        if (items != null && !items.isEmpty()) {
            order.items.addAll(items);
        }
        if (promotions != null && !promotions.isEmpty()) {
            order.promotions.addAll(promotions);
        }
        if (statusHistories != null && !statusHistories.isEmpty()) {
            order.statusHistories.addAll(statusHistories);
        }

        order.restoreAudit(
                createdAt,
                updatedAt,
                createdBy,
                updatedBy,
                deletedAt,
                version
        );

        return order;
    }

    public void addItem(OrderItem item) {
        ensureMutable();
        if (item == null) {
            throw new BadRequestException("Order item required");
        }
        this.items.add(item);
        recalculateTotals();
    }

    public void addPromotion(OrderPromotion promotion) {
        ensureMutable();
        if (promotion == null) {
            throw new BadRequestException("Order promotion required");
        }
        this.promotions.add(promotion);
    }

    public void addStatusHistory(OrderStatusHistory history) {
        if (history == null) {
            throw new BadRequestException("Order status history required");
        }
        this.statusHistories.add(history);
    }

    public void removeItemById(Long itemId) {
        ensureMutable();
        if (itemId == null) {
            return;
        }
        boolean removed = this.items.removeIf(i -> Objects.equals(i.getId(), itemId));
        if (removed) {
            recalculateTotals();
        }
    }

    public void clearItems() {
        ensureMutable();
        if (this.items.isEmpty()) {
            return;
        }
        this.items.clear();
        recalculateTotals();
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        ensureMutable();
        if (deliveryFee == null) {
            deliveryFee = BigDecimal.ZERO;
        }
        if (deliveryFee.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Delivery fee must be positive");
        }
        this.deliveryFee = deliveryFee;
        recalculateTotals();
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        ensureMutable();
        if (discountAmount == null) {
            discountAmount = BigDecimal.ZERO;
        }
        if (discountAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Discount amount must be positive");
        }
        this.discountAmount = discountAmount;
        recalculateTotals();
    }

    public void recalculateTotals() {
        this.subtotalAmount = this.items.stream()
                .map(OrderItem::getTotalPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal effectiveDiscount = discountAmount != null ? discountAmount : BigDecimal.ZERO;
        BigDecimal effectiveDeliveryFee = deliveryFee != null ? deliveryFee : BigDecimal.ZERO;

        BigDecimal total = subtotalAmount
                .subtract(effectiveDiscount)
                .add(effectiveDeliveryFee);

        if (total.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Order total amount cannot be negative");
        }

        this.totalAmount = total;
    }

    public void accept() {
        if (status != OrderStatus.PENDING) {
            throw new BadRequestException("Only pending orders can be accepted");
        }
        this.status = OrderStatus.ACCEPTED;
        this.acceptedAt = LocalDateTime.now();
    }

    public void startPreparing() {
        if (status != OrderStatus.ACCEPTED) {
            throw new BadRequestException("Only accepted orders can be moved to preparing");
        }
        this.status = OrderStatus.PREPARING;
        this.preparedAt = LocalDateTime.now();
    }

    public void markCompleted() {
        if (status != OrderStatus.READY && status != OrderStatus.DELIVERING) {
            throw new BadRequestException("Only ready or delivering orders can be completed");
        }
        this.status = OrderStatus.COMPLETED;
    }

    public void cancel(String reason) {
        if (FINAL_STATUSES.contains(status)) {
            throw new BadRequestException("Cannot cancel a completed or already cancelled order");
        }
        this.status = OrderStatus.CANCELLED;
        this.cancelReason = reason;
        this.cancelledAt = LocalDateTime.now();
    }

    private void ensureMutable() {
        if (FINAL_STATUSES.contains(status)) {
            throw new BadRequestException("Cannot modify a completed or cancelled order");
        }
    }
}
