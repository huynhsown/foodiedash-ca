package com.ute.foodiedash.interfaces.rest.inventory.mapper;

import com.ute.foodiedash.application.inventory.command.AdjustInventoryCommand;
import com.ute.foodiedash.application.inventory.command.ChangeInventoryStatusCommand;
import com.ute.foodiedash.application.inventory.command.ConsumeStockCommand;
import com.ute.foodiedash.application.inventory.command.CreateInventoryItemCommand;
import com.ute.foodiedash.application.inventory.command.MarkWasteCommand;
import com.ute.foodiedash.application.inventory.command.ReceiveStockCommand;
import com.ute.foodiedash.application.inventory.command.ReturnStockCommand;
import com.ute.foodiedash.application.inventory.command.UpdateInventoryItemCommand;
import com.ute.foodiedash.application.inventory.query.InventoryItemQueryResult;
import com.ute.foodiedash.interfaces.rest.inventory.dto.AdjustInventoryDTO;
import com.ute.foodiedash.interfaces.rest.inventory.dto.ChangeInventoryStatusDTO;
import com.ute.foodiedash.interfaces.rest.inventory.dto.ConsumeStockDTO;
import com.ute.foodiedash.interfaces.rest.inventory.dto.CreateInventoryItemDTO;
import com.ute.foodiedash.interfaces.rest.inventory.dto.InventoryItemResponseDTO;
import com.ute.foodiedash.interfaces.rest.inventory.dto.MarkWasteDTO;
import com.ute.foodiedash.interfaces.rest.inventory.dto.ReceiveStockDTO;
import com.ute.foodiedash.interfaces.rest.inventory.dto.ReturnStockDTO;
import com.ute.foodiedash.interfaces.rest.inventory.dto.UpdateInventoryItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryDtoMapper {

    CreateInventoryItemCommand toCommand(Long userId, CreateInventoryItemDTO dto);

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "id", source = "id")
    UpdateInventoryItemCommand toCommand(Long userId, Long id, UpdateInventoryItemDTO dto);

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "id", source = "id")
    ReceiveStockCommand toCommand(Long userId, Long id, ReceiveStockDTO dto);

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "id", source = "id")
    ConsumeStockCommand toCommand(Long userId, Long id, ConsumeStockDTO dto);

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "id", source = "id")
    MarkWasteCommand toCommand(Long userId, Long id, MarkWasteDTO dto);

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "id", source = "id")
    AdjustInventoryCommand toCommand(Long userId, Long id, AdjustInventoryDTO dto);

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "id", source = "id")
    ReturnStockCommand toCommand(Long userId, Long id, ReturnStockDTO dto);

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "id", source = "id")
    ChangeInventoryStatusCommand toCommand(Long userId, Long id, ChangeInventoryStatusDTO dto);

    InventoryItemResponseDTO toResponseDto(InventoryItemQueryResult result);
}
