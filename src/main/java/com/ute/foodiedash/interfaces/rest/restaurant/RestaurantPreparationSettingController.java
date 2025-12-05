package com.ute.foodiedash.interfaces.rest.restaurant;

import com.ute.foodiedash.application.restaurant.command.CreateRestaurantPreparationSettingCommand;
import com.ute.foodiedash.application.restaurant.query.RestaurantPreparationSettingQueryResult;
import com.ute.foodiedash.application.restaurant.usecase.CreateRestaurantPreparationSettingUseCase;
import com.ute.foodiedash.application.restaurant.usecase.RestoreRestaurantPreparationSettingUseCase;
import com.ute.foodiedash.application.restaurant.usecase.SoftDeleteRestaurantPreparationSettingUseCase;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.CreateRestaurantPreparationSettingDTO;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.RestaurantPreparationSettingResponseDTO;
import com.ute.foodiedash.interfaces.rest.restaurant.mapper.RestaurantPreparationSettingDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/restaurant-preparation-settings")
@RequiredArgsConstructor
public class RestaurantPreparationSettingController {

    private final CreateRestaurantPreparationSettingUseCase createRestaurantPreparationSettingUseCase;
    private final SoftDeleteRestaurantPreparationSettingUseCase softDeleteRestaurantPreparationSettingUseCase;
    private final RestoreRestaurantPreparationSettingUseCase restoreRestaurantPreparationSettingUseCase;
    private final RestaurantPreparationSettingDtoMapper dtoMapper;

    @PostMapping
    public ResponseEntity<RestaurantPreparationSettingResponseDTO> createRestaurantPreparationSetting(
            @Valid @RequestBody CreateRestaurantPreparationSettingDTO createRestaurantPreparationSettingDTO) {
        CreateRestaurantPreparationSettingCommand command = dtoMapper.toCommand(createRestaurantPreparationSettingDTO);
        RestaurantPreparationSettingQueryResult result = createRestaurantPreparationSettingUseCase.execute(command);
        RestaurantPreparationSettingResponseDTO response = dtoMapper.toResponseDto(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurantPreparationSetting(@PathVariable Long id) {
        softDeleteRestaurantPreparationSettingUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restoreRestaurantPreparationSetting(@PathVariable Long id) {
        restoreRestaurantPreparationSettingUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
