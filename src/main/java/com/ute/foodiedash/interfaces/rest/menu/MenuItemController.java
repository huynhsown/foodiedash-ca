package com.ute.foodiedash.interfaces.rest.menu;

import com.ute.foodiedash.application.menu.command.CreateMenuItemCommand;
import com.ute.foodiedash.application.menu.query.MenuItemQueryResult;
import com.ute.foodiedash.application.menu.usecase.CreateMenuItemUseCase;
import com.ute.foodiedash.application.menu.usecase.GetMenuItemByIdUseCase;
import com.ute.foodiedash.interfaces.rest.menu.dto.CreateMenuItemDTO;
import com.ute.foodiedash.interfaces.rest.menu.dto.MenuItemResponseDTO;
import com.ute.foodiedash.interfaces.rest.menu.mapper.MenuItemDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/menu-items")
@RequiredArgsConstructor
public class MenuItemController {

    private final CreateMenuItemUseCase createMenuItemUseCase;
    private final GetMenuItemByIdUseCase getMenuItemByIdUseCase;
    private final MenuItemDtoMapper dtoMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MenuItemResponseDTO> createMenuItem(
            @Valid @ModelAttribute CreateMenuItemDTO menuItemDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        CreateMenuItemCommand command = dtoMapper.toCommand(menuItemDTO);
        MenuItemQueryResult result = createMenuItemUseCase.execute(command, image);
        MenuItemResponseDTO response = dtoMapper.toResponseDto(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}/options")
    public ResponseEntity<MenuItemResponseDTO> getMenuItemOption(@PathVariable Long id) {
        MenuItemQueryResult result = getMenuItemByIdUseCase.execute(id);
        MenuItemResponseDTO response = dtoMapper.toResponseDtoWithOptions(result);
        return ResponseEntity.ok(response);
    }
}
