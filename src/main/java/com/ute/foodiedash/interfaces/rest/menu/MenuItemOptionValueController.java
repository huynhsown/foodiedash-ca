package com.ute.foodiedash.interfaces.rest.menu;

import com.ute.foodiedash.application.menu.command.CreateMenuItemOptionValueCommand;
import com.ute.foodiedash.application.menu.query.MenuItemOptionValueQueryResult;
import com.ute.foodiedash.application.menu.usecase.CreateMenuItemOptionValueUseCase;
import com.ute.foodiedash.interfaces.rest.menu.dto.CreateMenuItemOptionValueDTO;
import com.ute.foodiedash.interfaces.rest.menu.dto.MenuItemOptionValueResponseDTO;
import com.ute.foodiedash.interfaces.rest.menu.mapper.MenuItemOptionValueDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/menu-item-option-values")
@RequiredArgsConstructor
public class MenuItemOptionValueController {

    private final CreateMenuItemOptionValueUseCase createMenuItemOptionValueUseCase;
    private final MenuItemOptionValueDtoMapper dtoMapper;

    @PostMapping
    public ResponseEntity<MenuItemOptionValueResponseDTO> create(
            @Valid @RequestBody CreateMenuItemOptionValueDTO dto) {
        CreateMenuItemOptionValueCommand command = dtoMapper.toCommand(dto);
        MenuItemOptionValueQueryResult result = createMenuItemOptionValueUseCase.execute(command);
        MenuItemOptionValueResponseDTO response = dtoMapper.toResponseDto(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
