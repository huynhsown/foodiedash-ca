package com.ute.foodiedash.interfaces.rest.inventory;

import com.ute.foodiedash.application.inventory.InventoryFacade;
import com.ute.foodiedash.application.inventory.query.InventoryItemQueryResult;
import com.ute.foodiedash.infrastructure.security.SecurityContextHelper;
import com.ute.foodiedash.interfaces.rest.inventory.dto.AdjustInventoryDTO;
import com.ute.foodiedash.interfaces.rest.inventory.dto.ConsumeStockDTO;
import com.ute.foodiedash.interfaces.rest.inventory.dto.CreateInventoryItemDTO;
import com.ute.foodiedash.interfaces.rest.inventory.dto.InventoryItemResponseDTO;
import com.ute.foodiedash.interfaces.rest.inventory.dto.MarkWasteDTO;
import com.ute.foodiedash.interfaces.rest.inventory.dto.ReceiveStockDTO;
import com.ute.foodiedash.interfaces.rest.inventory.dto.ReturnStockDTO;
import com.ute.foodiedash.interfaces.rest.inventory.dto.UpdateInventoryItemDTO;
import com.ute.foodiedash.interfaces.rest.inventory.mapper.InventoryDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryFacade inventoryFacade;
    private final InventoryDtoMapper dtoMapper;

    @PostMapping
    public ResponseEntity<InventoryItemResponseDTO> createItem(
            @Valid @RequestBody CreateInventoryItemDTO dto) {
        Long userId = SecurityContextHelper.getCurrentUserId();
        var command = dtoMapper.toCommand(userId, dto);
        InventoryItemQueryResult result = inventoryFacade.create(command);
        InventoryItemResponseDTO response = dtoMapper.toResponseDto(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryItemResponseDTO> updateItem(
            @PathVariable Long id,
            @Valid @RequestBody UpdateInventoryItemDTO dto) {
        Long userId = SecurityContextHelper.getCurrentUserId();
        var command = dtoMapper.toCommand(userId, id, dto);
        InventoryItemQueryResult result = inventoryFacade.update(command);
        InventoryItemResponseDTO response = dtoMapper.toResponseDto(result);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/receive")
    public ResponseEntity<InventoryItemResponseDTO> receiveStock(
            @PathVariable Long id,
            @Valid @RequestBody ReceiveStockDTO dto) {
        Long userId = SecurityContextHelper.getCurrentUserId();
        var command = dtoMapper.toCommand(userId, id, dto);
        InventoryItemQueryResult result = inventoryFacade.receiveStock(command);
        InventoryItemResponseDTO response = dtoMapper.toResponseDto(result);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/consume")
    public ResponseEntity<InventoryItemResponseDTO> consumeStock(
            @PathVariable Long id,
            @Valid @RequestBody ConsumeStockDTO dto) {
        Long userId = SecurityContextHelper.getCurrentUserId();
        var command = dtoMapper.toCommand(userId, id, dto);
        InventoryItemQueryResult result = inventoryFacade.consumeStock(command);
        InventoryItemResponseDTO response = dtoMapper.toResponseDto(result);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/waste")
    public ResponseEntity<InventoryItemResponseDTO> markWaste(
            @PathVariable Long id,
            @Valid @RequestBody MarkWasteDTO dto) {
        Long userId = SecurityContextHelper.getCurrentUserId();
        var command = dtoMapper.toCommand(userId, id, dto);
        InventoryItemQueryResult result = inventoryFacade.markWaste(command);
        InventoryItemResponseDTO response = dtoMapper.toResponseDto(result);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/adjust")
    public ResponseEntity<InventoryItemResponseDTO> adjust(
            @PathVariable Long id,
            @Valid @RequestBody AdjustInventoryDTO dto) {
        Long userId = SecurityContextHelper.getCurrentUserId();
        var command = dtoMapper.toCommand(userId, id, dto);
        InventoryItemQueryResult result = inventoryFacade.adjust(command);
        InventoryItemResponseDTO response = dtoMapper.toResponseDto(result);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<InventoryItemResponseDTO> returnStock(
            @PathVariable Long id,
            @Valid @RequestBody ReturnStockDTO dto) {
        Long userId = SecurityContextHelper.getCurrentUserId();
        var command = dtoMapper.toCommand(userId, id, dto);
        InventoryItemQueryResult result = inventoryFacade.returnStock(command);
        InventoryItemResponseDTO response = dtoMapper.toResponseDto(result);
        return ResponseEntity.ok(response);
    }
}
