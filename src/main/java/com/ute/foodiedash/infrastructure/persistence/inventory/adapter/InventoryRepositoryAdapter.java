package com.ute.foodiedash.infrastructure.persistence.inventory.adapter;

import com.ute.foodiedash.domain.common.model.PageResult;
import com.ute.foodiedash.domain.inventory.enums.InventoryStatus;
import com.ute.foodiedash.domain.inventory.model.InventoryItem;
import com.ute.foodiedash.domain.inventory.model.InventoryTransaction;
import com.ute.foodiedash.domain.inventory.repository.InventoryRepository;
import com.ute.foodiedash.infrastructure.persistence.inventory.jpa.entity.InventoryItemJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.inventory.jpa.entity.InventoryTransactionJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.inventory.jpa.mapper.InventoryJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.inventory.jpa.repository.InventoryItemJpaRepository;
import com.ute.foodiedash.infrastructure.persistence.inventory.jpa.repository.InventoryTransactionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InventoryRepositoryAdapter implements InventoryRepository {
    private final InventoryItemJpaRepository itemJpaRepository;
    private final InventoryTransactionJpaRepository transactionJpaRepository;
    private final InventoryJpaMapper mapper;

    @Override
    public InventoryItem save(InventoryItem item) {
        var jpaEntity = item.getId() == null
                ? mapper.toJpaEntity(item)
                : itemJpaRepository.findById(item.getId())
                .map(existing -> {
                    mapper.updateEntity(existing, item);
                    return existing;
                })
                .orElseThrow();

        var saved = itemJpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<InventoryItem> findById(Long id) {
        return itemJpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<InventoryItem> findByRestaurantId(Long restaurantId) {
        return itemJpaRepository.findByRestaurantId(restaurantId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<InventoryItem> findBySkuAndRestaurantId(String sku, Long restaurantId) {
        return itemJpaRepository.findBySkuAndRestaurantId(sku, restaurantId)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsBySkuAndRestaurantId(String sku, Long restaurantId) {
        return itemJpaRepository.existsBySkuAndRestaurantId(sku, restaurantId);
    }

    @Override
    public List<InventoryItem> findLowStockItems(Long restaurantId) {
        return itemJpaRepository.findLowStockItems(restaurantId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void softDeleteById(Long id) {
        itemJpaRepository.softDeleteById(id);
    }

    @Override
    public void restoreById(Long id) {
        itemJpaRepository.restoreById(id);
    }

    @Override
    public InventoryTransaction saveTransaction(InventoryTransaction transaction) {
        InventoryTransactionJpaEntity jpaEntity;
        if (transaction.getId() == null) {
            jpaEntity = mapper.toJpaEntity(transaction);
            var itemRef = itemJpaRepository.getReferenceById(transaction.getInventoryItemId());
            jpaEntity.setInventoryItem(itemRef);
        } else {
            jpaEntity = transactionJpaRepository.findById(transaction.getId())
                    .orElseThrow();
            updateTransactionEntity(jpaEntity, transaction);
        }
        var saved = transactionJpaRepository.save(jpaEntity);
        return transactionToDomain(saved);
    }

    @Override
    public List<InventoryTransaction> findTransactionsByItemId(Long inventoryItemId) {
        return transactionJpaRepository.findByInventoryItemIdOrderByCreatedAtAsc(inventoryItemId).stream()
                .map(this::transactionToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<InventoryItem> search(String keyword, InventoryStatus status, Boolean lowStock,
                                            Long restaurantId, Long id,
                                            Integer page, Integer size,
                                            String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<InventoryItemJpaEntity> jpaPage = itemJpaRepository.search(
                keyword, status, lowStock, restaurantId, id, pageable
        );

        return new PageResult<>(
                jpaPage.stream().map(mapper::toDomain).toList(),
                jpaPage.getNumber(),
                jpaPage.getSize(),
                jpaPage.getTotalElements(),
                jpaPage.getTotalPages()
        );
    }

    private InventoryTransaction transactionToDomain(InventoryTransactionJpaEntity e) {
        if (e == null) return null;
        return InventoryTransaction.reconstruct(
                e.getId(),
                e.getInventoryItem().getId(),
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

    private void updateTransactionEntity(InventoryTransactionJpaEntity e, InventoryTransaction domain) {
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
}
