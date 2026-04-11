package com.ute.foodiedash.application.inventory.usecase;

import com.ute.foodiedash.application.inventory.command.ListInventoryItemsCommand;
import com.ute.foodiedash.application.inventory.query.InventoryItemQueryResult;
import com.ute.foodiedash.domain.common.exception.ForbiddenException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.common.model.PageResult;
import com.ute.foodiedash.domain.inventory.model.InventoryItem;
import com.ute.foodiedash.domain.inventory.repository.InventoryRepository;
import com.ute.foodiedash.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ListInventoryItemsUseCase {

    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public PageResult<InventoryItemQueryResult> execute(ListInventoryItemsCommand command) {
        Long restaurantId = command.restaurantId();

        if (restaurantId != null
                && !userRepository.existsMerchantRestaurant(command.userId(), restaurantId)) {
            throw new ForbiddenException("User does not have permission to access this restaurant");
        }

        PageResult<InventoryItem> page = inventoryRepository.search(
                command.keyword(),
                command.status(),
                command.lowStock(),
                restaurantId,
                command.id(),
                command.page(),
                command.size(),
                command.sortBy(),
                command.sortDirection()
        );

        return new PageResult<>(
                page.getContent().stream()
                        .map(InventoryItemQueryResult::from)
                        .toList(),
                page.getPage(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
