package com.ute.foodiedash.infrastructure.persistence.inventory.jpa.entity;

import com.ute.foodiedash.domain.inventory.enums.InventoryReferenceType;
import com.ute.foodiedash.domain.inventory.enums.InventoryTransactionType;
import com.ute.foodiedash.infrastructure.persistence.common.jpa.entity.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "inventory_transactions")
public class InventoryTransactionJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_item_id", nullable = false)
    private InventoryItemJpaEntity inventoryItem;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 20)
    private InventoryTransactionType transactionType;

    @Column(name = "quantity_change", precision = 14, scale = 3, nullable = false)
    private BigDecimal quantityChange;

    @Column(name = "quantity_before", precision = 14, scale = 3, nullable = false)
    private BigDecimal quantityBefore;

    @Column(name = "quantity_after", precision = 14, scale = 3, nullable = false)
    private BigDecimal quantityAfter;

    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type", nullable = false, length = 20)
    private InventoryReferenceType referenceType;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(columnDefinition = "text")
    private String note;
}
