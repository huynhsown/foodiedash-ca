package com.ute.foodiedash.interfaces.rest.menu;

import com.ute.foodiedash.application.menu.command.CreateMenuItemOptionCommand;
import com.ute.foodiedash.application.menu.query.MenuItemOptionQueryResult;
import com.ute.foodiedash.application.menu.usecase.CreateMenuItemOptionUseCase;
import com.ute.foodiedash.interfaces.rest.menu.dto.CreateMenuItemOptionDTO;
import com.ute.foodiedash.interfaces.rest.menu.dto.MenuItemOptionResponseDTO;
import com.ute.foodiedash.interfaces.rest.menu.mapper.MenuItemOptionDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/menu-item-options")
@RequiredArgsConstructor
public class MenuItemOptionController {

    private final CreateMenuItemOptionUseCase createMenuItemOptionUseCase;
    private final MenuItemOptionDtoMapper dtoMapper;

    @PostMapping
    public ResponseEntity<MenuItemOptionResponseDTO> create(
            @Valid @RequestBody CreateMenuItemOptionDTO dto) {
        CreateMenuItemOptionCommand command = dtoMapper.toCommand(dto);
        MenuItemOptionQueryResult result = createMenuItemOptionUseCase.execute(command);
        MenuItemOptionResponseDTO response = dtoMapper.toResponseDto(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
