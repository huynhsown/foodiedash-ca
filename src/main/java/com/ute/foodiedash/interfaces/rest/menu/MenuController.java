package com.ute.foodiedash.interfaces.rest.menu;

import com.ute.foodiedash.application.menu.command.CreateMenuCommand;
import com.ute.foodiedash.application.menu.query.MenuQueryResult;
import com.ute.foodiedash.application.menu.usecase.CreateMenuUseCase;
import com.ute.foodiedash.application.menu.usecase.RestoreMenuUseCase;
import com.ute.foodiedash.application.menu.usecase.SoftDeleteMenuUseCase;
import com.ute.foodiedash.application.menu.usecase.SubmitMenuUseCase;
import com.ute.foodiedash.interfaces.rest.menu.dto.CreateMenuDTO;
import com.ute.foodiedash.interfaces.rest.menu.dto.MenuResponseDTO;
import com.ute.foodiedash.interfaces.rest.menu.mapper.MenuDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
public class MenuController {
    private final CreateMenuUseCase createMenuUseCase;
    private final SoftDeleteMenuUseCase softDeleteMenuUseCase;
    private final RestoreMenuUseCase restoreMenuUseCase;
    private final SubmitMenuUseCase submitMenuUseCase;
    private final MenuDtoMapper dtoMapper;

    @PostMapping
    public ResponseEntity<MenuResponseDTO> createMenu(@Valid @RequestBody CreateMenuDTO createMenuDTO) {
        CreateMenuCommand command = dtoMapper.toCommand(createMenuDTO);
        MenuQueryResult result = createMenuUseCase.execute(command);
        MenuResponseDTO response = dtoMapper.toResponseDto(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
        softDeleteMenuUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restoreMenu(@PathVariable Long id) {
        restoreMenuUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}/submit")
    public ResponseEntity<MenuResponseDTO> submitMenu(@PathVariable Long id) {
        MenuQueryResult result = submitMenuUseCase.execute(id);
        MenuResponseDTO response = dtoMapper.toResponseDto(result);
        return ResponseEntity.ok(response);
    }
}
