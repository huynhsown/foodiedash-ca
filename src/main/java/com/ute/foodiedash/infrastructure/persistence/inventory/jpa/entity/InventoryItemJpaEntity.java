package com.ute.foodiedash.infrastructure.persistence.inventory.jpa.entity;

import com.ute.foodiedash.domain.inventory.enums.InventoryStatus;
import com.ute.foodiedash.domain.inventory.enums.InventoryUnit;
import com.ute.foodiedash.infrastructure.persistence.common.jpa.entity.BaseJpaEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "inventory_items")
public class InventoryItemJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;

    @Column(nullable = false, length = 100)
    private String sku;

    @Column(nullable = false, length = 255)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InventoryUnit unit;

    @Column(name = "quantity_on_hand", precision = 14, scale = 3, nullable = false)
    private BigDecimal quantityOnHand;

    @Column(name = "reorder_level", precision = 14, scale = 3)
    private BigDecimal reorderLevel;

    @Column(name = "reorder_quantity", precision = 14, scale = 3)
    private BigDecimal reorderQuantity;

    @Column(name = "unit_cost", precision = 12, scale = 2)
    private BigDecimal unitCost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InventoryStatus status;

    @OneToMany(mappedBy = "inventoryItem", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @BatchSize(size = 50)
    private List<InventoryTransactionJpaEntity> transactions = new ArrayList<>();
}
