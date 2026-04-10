package com.ute.foodiedash.application.inventory.usecase;

import com.ute.foodiedash.application.inventory.command.ReturnStockCommand;
import com.ute.foodiedash.application.inventory.query.InventoryItemQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.inventory.model.InventoryItem;
import com.ute.foodiedash.domain.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReturnStockUseCase {
    private final InventoryRepository inventoryRepository;

    @Transactional
    public InventoryItemQueryResult execute(ReturnStockCommand command) {
        InventoryItem item = inventoryRepository.findById(command.id())
                .orElseThrow(() -> new NotFoundException("Inventory item not found with id " + command.id()));

        item.returnStock(command.quantity(), command.orderId(), command.note());

        InventoryItem saved = inventoryRepository.save(item);

        return new InventoryItemQueryResult(
                saved.getId(),
                saved.getRestaurantId(),
                saved.getSku(),
                saved.getName(),
                saved.getUnit(),
                saved.getQuantityOnHand(),
                saved.getReorderLevel(),
                saved.getReorderQuantity(),
                saved.getUnitCost(),
                saved.getStatus(),
                saved.isLowStock()
        );
    }
}
