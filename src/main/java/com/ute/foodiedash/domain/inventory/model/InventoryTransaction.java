package com.ute.foodiedash.domain.inventory.model;

import com.ute.foodiedash.domain.common.model.BaseEntity;
import com.ute.foodiedash.domain.inventory.enums.InventoryReferenceType;
import com.ute.foodiedash.domain.inventory.enums.InventoryTransactionType;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
public class InventoryTransaction extends BaseEntity {
    private Long id;
    private Long inventoryItemId;
    private InventoryTransactionType transactionType;
    private BigDecimal quantityChange;
    private BigDecimal quantityBefore;
    private BigDecimal quantityAfter;
    private InventoryReferenceType referenceType;
    private Long referenceId;
    private String note;

    private InventoryTransaction() {}

    private InventoryTransaction(
            Long inventoryItemId,
            InventoryTransactionType transactionType,
            BigDecimal quantityChange,
            BigDecimal quantityBefore,
            BigDecimal quantityAfter,
            InventoryReferenceType referenceType,
            Long referenceId,
            String note
    ) {
        this.inventoryItemId = inventoryItemId;
        this.transactionType = transactionType;
        this.quantityChange = quantityChange;
        this.quantityBefore = quantityBefore;
        this.quantityAfter = quantityAfter;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.note = note;
    }

    public static InventoryTransaction reconstruct(
            Long id,
            Long inventoryItemId,
            InventoryTransactionType transactionType,
            BigDecimal quantityChange,
            BigDecimal quantityBefore,
            BigDecimal quantityAfter,
            InventoryReferenceType referenceType,
            Long referenceId,
            String note,
            Instant createdAt,
            Instant updatedAt,
            String createdBy,
            String updatedBy,
            Instant deletedAt,
            Long version
    ) {
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.id = id;
        transaction.inventoryItemId = inventoryItemId;
        transaction.transactionType = transactionType;
        transaction.quantityChange = quantityChange;
        transaction.quantityBefore = quantityBefore;
        transaction.quantityAfter = quantityAfter;
        transaction.referenceType = referenceType;
        transaction.referenceId = referenceId;
        transaction.note = note;
        transaction.restoreAudit(createdAt, updatedAt, createdBy, updatedBy, deletedAt, version);
        return transaction;
    }

    public static InventoryTransaction purchase(
            Long inventoryItemId,
            BigDecimal quantity,
            BigDecimal quantityBefore,
            BigDecimal quantityAfter,
            Long purchaseOrderId,
            String note
    ) {
        return new InventoryTransaction(
                inventoryItemId,
                InventoryTransactionType.PURCHASE,
                quantity,
                quantityBefore,
                quantityAfter,
                InventoryReferenceType.PURCHASE_ORDER,
                purchaseOrderId,
                note
        );
    }

    public static InventoryTransaction sale(
            Long inventoryItemId,
            BigDecimal quantity,
            BigDecimal quantityBefore,
            BigDecimal quantityAfter,
            Long orderId,
            String note
    ) {
        return new InventoryTransaction(
                inventoryItemId,
                InventoryTransactionType.SALE,
                quantity.negate(),
                quantityBefore,
                quantityAfter,
                InventoryReferenceType.ORDER,
                orderId,
                note
        );
    }

    public static InventoryTransaction waste(
            Long inventoryItemId,
            BigDecimal quantity,
            BigDecimal quantityBefore,
            BigDecimal quantityAfter,
            String reason
    ) {
        return new InventoryTransaction(
                inventoryItemId,
                InventoryTransactionType.WASTE,
                quantity.negate(),
                quantityBefore,
                quantityAfter,
                InventoryReferenceType.MANUAL,
                null,
                reason
        );
    }

    public static InventoryTransaction adjustment(
            Long inventoryItemId,
            BigDecimal quantityChange,
            BigDecimal quantityBefore,
            BigDecimal quantityAfter,
            String note
    ) {
        return new InventoryTransaction(
                inventoryItemId,
                InventoryTransactionType.ADJUSTMENT,
                quantityChange,
                quantityBefore,
                quantityAfter,
                InventoryReferenceType.MANUAL,
                null,
                note
        );
    }

    public static InventoryTransaction returned(
            Long inventoryItemId,
            BigDecimal quantity,
            BigDecimal quantityBefore,
            BigDecimal quantityAfter,
            Long orderId,
            String note
    ) {
        return new InventoryTransaction(
                inventoryItemId,
                InventoryTransactionType.RETURN,
                quantity,
                quantityBefore,
                quantityAfter,
                InventoryReferenceType.ORDER,
                orderId,
                note
        );
    }
}
