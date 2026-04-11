package com.ute.foodiedash.application.inventory.usecase;

import com.ute.foodiedash.application.inventory.command.CreateInventoryItemCommand;
import com.ute.foodiedash.application.inventory.query.InventoryItemQueryResult;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
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
public class CreateInventoryItemUseCase {
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public InventoryItemQueryResult execute(CreateInventoryItemCommand command) {

        if (!userRepository.existsMerchantRestaurant(command.userId(), command.restaurantId())) {
            throw new ForbiddenException(
                    "User does not have permission to access this restaurant"
            );
        }

        if (inventoryRepository.existsBySkuAndRestaurantId(command.sku(), command.restaurantId())) {
            throw new BadRequestException("SKU already exists for this restaurant.");
        }

        InventoryItem item = InventoryItem.create(
                command.restaurantId(),
                command.sku(),
                command.name(),
                command.unit(),
                command.reorderLevel(),
                command.reorderQuantity(),
                command.unitCost()
        );

        InventoryItem saved = inventoryRepository.save(item);

        return InventoryItemQueryResult.from(saved);
    }
}
