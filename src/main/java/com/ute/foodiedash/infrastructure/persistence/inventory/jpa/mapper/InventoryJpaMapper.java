package com.ute.foodiedash.infrastructure.persistence.inventory.jpa.mapper;

import com.ute.foodiedash.domain.inventory.model.InventoryItem;
import com.ute.foodiedash.domain.inventory.model.InventoryTransaction;
import com.ute.foodiedash.infrastructure.persistence.inventory.jpa.entity.InventoryItemJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.inventory.jpa.entity.InventoryTransactionJpaEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class InventoryJpaMapper {

    public InventoryItem toDomain(InventoryItemJpaEntity e) {
        if (e == null) return null;

        List<InventoryTransaction> transactions = e.getTransactions() != null
                ? e.getTransactions().stream()
                .map(this::transactionToDomain)
                .collect(Collectors.toList())
                : Collections.emptyList();

        return InventoryItem.reconstruct(
                e.getId(),
                e.getRestaurantId(),
                e.getSku(),
                e.getName(),
                e.getUnit(),
                e.getQuantityOnHand(),
                e.getReorderLevel(),
                e.getReorderQuantity(),
                e.getUnitCost(),
                e.getStatus(),
                transactions,
                e.getCreatedAt(),
                e.getUpdatedAt(),
                e.getCreatedBy(),
                e.getUpdatedBy(),
                e.getDeletedAt(),
                e.getVersion()
        );
    }

    private InventoryTransaction transactionToDomain(InventoryTransactionJpaEntity e) {
        if (e == null) return null;

        return InventoryTransaction.reconstruct(
                e.getId(),
                e.getInventoryItem() != null ? e.getInventoryItem().getId() : null,
                e.getTransactionType(),
                e.getQuantityChange(),
                e.getQuantityBefore(),
                e.getQuantityAfter(),
                e.getReferenceType(),
                e.getReferenceId(),
                e.getNote(),
                e.getCreatedAt(),
                e.getUpdatedAt(),
                e.getCreatedBy(),
                e.getUpdatedBy(),
                e.getDeletedAt(),
                e.getVersion()
        );
    }

    @Mapping(target = "transactions", ignore = true)
    public abstract InventoryItemJpaEntity toJpaEntity(InventoryItem domain);

    public abstract InventoryTransactionJpaEntity toJpaEntity(InventoryTransaction domain);

    public void updateEntity(@MappingTarget InventoryItemJpaEntity e, InventoryItem domain) {
        e.setRestaurantId(domain.getRestaurantId());
        e.setSku(domain.getSku());
        e.setName(domain.getName());
        e.setUnit(domain.getUnit());
        e.setQuantityOnHand(domain.getQuantityOnHand());
        e.setReorderLevel(domain.getReorderLevel());
        e.setReorderQuantity(domain.getReorderQuantity());
        e.setUnitCost(domain.getUnitCost());
        e.setStatus(domain.getStatus());
        mergeTransactions(e, domain);
        e.setDeletedAt(domain.getDeletedAt());
        e.setVersion(domain.getVersion());
    }

    private void mergeTransactions(InventoryItemJpaEntity e, InventoryItem domain) {
        if (domain.getTransactions() == null) {
            e.getTransactions().clear();
            return;
        }

        var existing = e.getTransactions();
        var domainTxIds = domain.getTransactions().stream()
                .map(InventoryTransaction::getId)
                .filter(java.util.Objects::nonNull)
                .collect(java.util.stream.Collectors.toSet());

        existing.removeIf(tx -> !domainTxIds.contains(tx.getId()));

        var existingById = existing.stream()
                .collect(java.util.stream.Collectors.toMap(
                        InventoryTransactionJpaEntity::getId,
                        java.util.function.Function.identity()
                ));

        for (InventoryTransaction domainTx : domain.getTransactions()) {
            if (domainTx.getId() != null && existingById.containsKey(domainTx.getId())) {
                updateTransactionEntity(existingById.get(domainTx.getId()), domainTx);
            } else {
                InventoryTransactionJpaEntity jpaTx = toJpaEntity(domainTx);
                jpaTx.setInventoryItem(e);
                existing.add(jpaTx);
            }
        }
    }

    private void updateTransactionEntity(@MappingTarget InventoryTransactionJpaEntity e, InventoryTransaction domain) {
        e.setTransactionType(domain.getTransactionType());
        e.setQuantityChange(domain.getQuantityChange());
        e.setQuantityBefore(domain.getQuantityBefore());
        e.setQuantityAfter(domain.getQuantityAfter());
        e.setReferenceType(domain.getReferenceType());
        e.setReferenceId(domain.getReferenceId());
        e.setNote(domain.getNote());
        e.setDeletedAt(domain.getDeletedAt());
        e.setVersion(domain.getVersion());
    }

    @AfterMapping
    protected void setItemReferences(@MappingTarget InventoryItemJpaEntity jpaEntity) {
        if (jpaEntity.getTransactions() != null && !jpaEntity.getTransactions().isEmpty()) {
            jpaEntity.getTransactions().forEach(tx -> tx.setInventoryItem(jpaEntity));
        }
    }
}
