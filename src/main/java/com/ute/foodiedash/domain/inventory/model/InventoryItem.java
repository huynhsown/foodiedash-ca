package com.ute.foodiedash.domain.inventory.model;

import com.ute.foodiedash.domain.common.model.BaseEntity;
import com.ute.foodiedash.domain.inventory.enums.InventoryStatus;
import com.ute.foodiedash.domain.inventory.enums.InventoryUnit;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
public class InventoryItem extends BaseEntity {
    private Long id;
    private Long restaurantId;
    private String sku;
    private String name;
    private InventoryUnit unit;
    private BigDecimal quantityOnHand;
    private BigDecimal reorderLevel;
    private BigDecimal reorderQuantity;
    private BigDecimal unitCost;
    private InventoryStatus status;
    private final List<InventoryTransaction> transactions = new ArrayList<>();

    private InventoryItem() {}

    private InventoryItem(
            Long restaurantId,
            String sku,
            String name,
            InventoryUnit unit,
            BigDecimal reorderLevel,
            BigDecimal reorderQuantity,
            BigDecimal unitCost
    ) {
        this.restaurantId = restaurantId;
        this.sku = sku;
        this.name = name;
        this.unit = unit;
        this.quantityOnHand = BigDecimal.ZERO;
        this.reorderLevel = reorderLevel;
        this.reorderQuantity = reorderQuantity;
        this.unitCost = unitCost;
        this.status = InventoryStatus.ACTIVE;
    }

    public static InventoryItem reconstruct(
            Long id,
            Long restaurantId,
            String sku,
            String name,
            InventoryUnit unit,
            BigDecimal quantityOnHand,
            BigDecimal reorderLevel,
            BigDecimal reorderQuantity,
            BigDecimal unitCost,
            InventoryStatus status,
            List<InventoryTransaction> transactions,
            Instant createdAt,
            Instant updatedAt,
            String createdBy,
            String updatedBy,
            Instant deletedAt,
            Long version
    ) {
        InventoryItem item = new InventoryItem();
        item.id = id;
        item.restaurantId = restaurantId;
        item.sku = sku;
        item.name = name;
        item.unit = unit;
        item.quantityOnHand = quantityOnHand;
        item.reorderLevel = reorderLevel;
        item.reorderQuantity = reorderQuantity;
        item.unitCost = unitCost;
        item.status = status;
        item.transactions.addAll(transactions);
        item.restoreAudit(createdAt, updatedAt, createdBy, updatedBy, deletedAt, version);
        return item;
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Inventory item name is required.");
        }
    }

    private static void validateUnitCost(BigDecimal unitCost) {
        if (unitCost == null || unitCost.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Unit cost must be greater than zero.");
        }
    }

    private static void validateReorderLevel(BigDecimal reorderLevel) {
        if (reorderLevel == null || reorderLevel.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Reorder level cannot be negative.");
        }
    }

    private static void validateReorderQuantity(BigDecimal reorderQuantity) {
        if (reorderQuantity == null || reorderQuantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Reorder quantity cannot be negative.");
        }
    }

    public static InventoryItem create(
            Long restaurantId,
            String sku,
            String name,
            InventoryUnit unit,
            BigDecimal reorderLevel,
            BigDecimal reorderQuantity,
            BigDecimal unitCost
    ) {
        if (restaurantId == null) {
            throw new IllegalArgumentException("Restaurant ID is required.");
        }

        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU is required.");
        }

        validateName(name);

        if (unit == null) {
            throw new IllegalArgumentException("Inventory unit is required.");
        }

        validateReorderLevel(reorderLevel);
        validateReorderQuantity(reorderQuantity);
        validateUnitCost(unitCost);

        return new InventoryItem(
                restaurantId,
                sku.trim(),
                name.trim(),
                unit,
                reorderLevel,
                reorderQuantity,
                unitCost
        );
    }

    private void ensureActive() {
        if (status != InventoryStatus.ACTIVE) {
            throw new IllegalStateException("Inventory item is inactive.");
        }
    }

    private void ensurePositive(BigDecimal quantity) {
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
    }

    private void ensureEnoughStock(BigDecimal quantity) {
        if (quantityOnHand.compareTo(quantity) < 0) {
            throw new IllegalStateException("Insufficient inventory.");
        }
    }

    public void receive(
            BigDecimal quantity,
            Long purchaseOrderId,
            String note
    ) {
        ensureActive();
        ensurePositive(quantity);

        BigDecimal quantityBefore = quantityOnHand;
        quantityOnHand = quantityBefore.add(quantity);

        transactions.add(InventoryTransaction.purchase(
                id,
                quantity,
                quantityBefore,
                quantityOnHand,
                purchaseOrderId,
                note
        ));
    }

    public void consume(
            BigDecimal quantity,
            Long orderId,
            String note
    ) {
        ensureActive();
        ensurePositive(quantity);
        ensureEnoughStock(quantity);

        BigDecimal before = quantityOnHand;

        quantityOnHand = quantityOnHand.subtract(quantity);

        transactions.add(
                InventoryTransaction.sale(
                        id,
                        quantity,
                        before,
                        quantityOnHand,
                        orderId,
                        note
                )
        );
    }

    public void markWaste(
            BigDecimal quantity,
            String reason
    ) {
        ensureActive();
        ensurePositive(quantity);
        ensureEnoughStock(quantity);

        BigDecimal before = quantityOnHand;

        quantityOnHand = quantityOnHand.subtract(quantity);

        transactions.add(
                InventoryTransaction.waste(
                        id,
                        quantity,
                        before,
                        quantityOnHand,
                        reason
                )
        );
    }

    public void adjust(
            BigDecimal quantityChange,
            String note
    ) {
        ensureActive();

        if (quantityChange == null ||
                quantityChange.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException(
                    "Adjustment cannot be zero."
            );
        }

        BigDecimal after = quantityOnHand.add(quantityChange);
        ensurePositive(after);

        BigDecimal before = quantityOnHand;

        quantityOnHand = after;

        transactions.add(
                InventoryTransaction.adjustment(
                        id,
                        quantityChange,
                        before,
                        quantityOnHand,
                        note
                )
        );
    }

    public void returnStock(
            BigDecimal quantity,
            Long orderId,
            String note
    ) {
        ensureActive();
        ensurePositive(quantity);

        BigDecimal before = quantityOnHand;

        quantityOnHand = quantityOnHand.add(quantity);

        transactions.add(
                InventoryTransaction.returned(
                        id,
                        quantity,
                        before,
                        quantityOnHand,
                        orderId,
                        note
                )
        );
    }

    public void activate() {
        if (status == InventoryStatus.DISCONTINUED) {
            throw new IllegalStateException(
                    "Discontinued inventory item cannot be activated."
            );
        }
        this.status = InventoryStatus.ACTIVE;
    }

    public void deactivate() {
        if (status == InventoryStatus.DISCONTINUED) {
            throw new IllegalStateException(
                    "Discontinued inventory item cannot be activated."
            );
        }
        status = InventoryStatus.INACTIVE;
    }

    public void discontinued () {
        this.status = InventoryStatus.DISCONTINUED;
    }

    public void rename(String name) {
        validateName(name);
        this.name = name.trim();
    }

    public void updateUnitCost(BigDecimal unitCost) {
        validateUnitCost(unitCost);
        this.unitCost = unitCost;
    }

    public void updateReorderPolicy(
            BigDecimal reorderLevel,
            BigDecimal reorderQuantity
    ) {
        validateReorderLevel(reorderLevel);
        validateReorderQuantity(reorderQuantity);
        this.reorderLevel = reorderLevel;
        this.reorderQuantity = reorderQuantity;
    }

    public boolean isLowStock() {
        return quantityOnHand.compareTo(reorderLevel) <= 0;
    }
}
