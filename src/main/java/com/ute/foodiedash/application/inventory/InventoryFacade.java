package com.ute.foodiedash.application.inventory;

import com.ute.foodiedash.application.inventory.command.AdjustInventoryCommand;
import com.ute.foodiedash.application.inventory.command.ConsumeStockCommand;
import com.ute.foodiedash.application.inventory.command.CreateInventoryItemCommand;
import com.ute.foodiedash.application.inventory.command.MarkWasteCommand;
import com.ute.foodiedash.application.inventory.command.ReceiveStockCommand;
import com.ute.foodiedash.application.inventory.command.ReturnStockCommand;
import com.ute.foodiedash.application.inventory.command.UpdateInventoryItemCommand;
import com.ute.foodiedash.application.inventory.query.InventoryItemQueryResult;
import com.ute.foodiedash.application.inventory.usecase.AdjustInventoryUseCase;
import com.ute.foodiedash.application.inventory.usecase.ConsumeStockUseCase;
import com.ute.foodiedash.application.inventory.usecase.CreateInventoryItemUseCase;
import com.ute.foodiedash.application.inventory.usecase.MarkWasteUseCase;
import com.ute.foodiedash.application.inventory.usecase.ReceiveStockUseCase;
import com.ute.foodiedash.application.inventory.usecase.ReturnStockUseCase;
import com.ute.foodiedash.application.inventory.usecase.UpdateInventoryItemUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryFacade {
    private final CreateInventoryItemUseCase createInventoryItemUseCase;
    private final UpdateInventoryItemUseCase updateInventoryItemUseCase;
    private final ReceiveStockUseCase receiveStockUseCase;
    private final ConsumeStockUseCase consumeStockUseCase;
    private final MarkWasteUseCase markWasteUseCase;
    private final AdjustInventoryUseCase adjustInventoryUseCase;
    private final ReturnStockUseCase returnStockUseCase;

    public InventoryItemQueryResult create(CreateInventoryItemCommand command) {
        return createInventoryItemUseCase.execute(command);
    }

    public InventoryItemQueryResult update(UpdateInventoryItemCommand command) {
        return updateInventoryItemUseCase.execute(command);
    }

    public InventoryItemQueryResult receiveStock(ReceiveStockCommand command) {
        return receiveStockUseCase.execute(command);
    }

    public InventoryItemQueryResult consumeStock(ConsumeStockCommand command) {
        return consumeStockUseCase.execute(command);
    }

    public InventoryItemQueryResult markWaste(MarkWasteCommand command) {
        return markWasteUseCase.execute(command);
    }

    public InventoryItemQueryResult adjust(AdjustInventoryCommand command) {
        return adjustInventoryUseCase.execute(command);
    }

    public InventoryItemQueryResult returnStock(ReturnStockCommand command) {
        return returnStockUseCase.execute(command);
    }
}
