package com.ute.foodiedash.application.inventory.usecase;

import com.ute.foodiedash.application.inventory.command.UpdateInventoryItemCommand;
import com.ute.foodiedash.application.inventory.query.InventoryItemQueryResult;
import com.ute.foodiedash.domain.common.exception.ForbiddenException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.inventory.model.InventoryItem;
import com.ute.foodiedash.domain.inventory.repository.InventoryRepository;
import com.ute.foodiedash.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UpdateInventoryItemUseCase {
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public InventoryItemQueryResult execute(UpdateInventoryItemCommand command) {
        InventoryItem item = inventoryRepository.findById(command.id())
                .orElseThrow(() -> new NotFoundException("Inventory item not found with id " + command.id()));

        if (!userRepository.existsMerchantRestaurant(command.userId(), item.getRestaurantId())) {
            throw new ForbiddenException(
                    "User does not have permission to access this restaurant"
            );
        }

        item.rename(command.name());
        item.updateUnitCost(command.unitCost());
        item.updateReorderPolicy(command.reorderLevel(), command.reorderQuantity());

        InventoryItem saved = inventoryRepository.save(item);

        return InventoryItemQueryResult.from(saved);
    }
}
