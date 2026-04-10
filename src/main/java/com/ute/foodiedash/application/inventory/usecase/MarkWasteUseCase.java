package com.ute.foodiedash.application.inventory.usecase;

import com.ute.foodiedash.application.inventory.command.MarkWasteCommand;
import com.ute.foodiedash.application.inventory.query.InventoryItemQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.inventory.model.InventoryItem;
import com.ute.foodiedash.domain.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MarkWasteUseCase {
    private final InventoryRepository inventoryRepository;

    @Transactional
    public InventoryItemQueryResult execute(MarkWasteCommand command) {
        InventoryItem item = inventoryRepository.findById(command.id())
                .orElseThrow(() -> new NotFoundException("Inventory item not found with id " + command.id()));

        item.markWaste(command.quantity(), command.reason());

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
