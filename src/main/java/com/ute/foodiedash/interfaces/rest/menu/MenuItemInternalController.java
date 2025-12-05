package com.ute.foodiedash.interfaces.rest.menu;

import com.ute.foodiedash.application.menu.query.MenuItemQueryResult;
import com.ute.foodiedash.application.menu.usecase.GetMenuItemByIdUseCase;
import com.ute.foodiedash.interfaces.rest.menu.dto.MenuItemResponseDTO;
import com.ute.foodiedash.interfaces.rest.menu.mapper.MenuItemDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/internal/menu-items")
@RequiredArgsConstructor
public class MenuItemInternalController {

    private final GetMenuItemByIdUseCase getMenuItemByIdUseCase;
    private final MenuItemDtoMapper dtoMapper;

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponseDTO> getMenuItemById(@PathVariable Long id) {
        MenuItemQueryResult result = getMenuItemByIdUseCase.execute(id);
        MenuItemResponseDTO response = dtoMapper.toResponseDto(result);
        return ResponseEntity.ok(response);
    }
}
