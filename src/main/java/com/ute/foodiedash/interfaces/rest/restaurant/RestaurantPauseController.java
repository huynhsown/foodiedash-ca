package com.ute.foodiedash.interfaces.rest.restaurant;

import com.ute.foodiedash.application.restaurant.command.CreateRestaurantPauseCommand;
import com.ute.foodiedash.application.restaurant.query.RestaurantPauseQueryResult;
import com.ute.foodiedash.application.restaurant.usecase.CreateRestaurantPauseUseCase;
import com.ute.foodiedash.application.restaurant.usecase.RestoreRestaurantPauseUseCase;
import com.ute.foodiedash.application.restaurant.usecase.SoftDeleteRestaurantPauseUseCase;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.CreateRestaurantPauseDTO;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.RestaurantPauseResponseDTO;
import com.ute.foodiedash.interfaces.rest.restaurant.mapper.RestaurantPauseDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/restaurant-pauses")
@RequiredArgsConstructor
public class RestaurantPauseController {

    private final CreateRestaurantPauseUseCase createRestaurantPauseUseCase;
    private final SoftDeleteRestaurantPauseUseCase softDeleteRestaurantPauseUseCase;
    private final RestoreRestaurantPauseUseCase restoreRestaurantPauseUseCase;
    private final RestaurantPauseDtoMapper dtoMapper;

    @PostMapping
    public ResponseEntity<RestaurantPauseResponseDTO> createRestaurantPause(
            @Valid @RequestBody CreateRestaurantPauseDTO createRestaurantPauseDTO) {
        CreateRestaurantPauseCommand command = dtoMapper.toCommand(createRestaurantPauseDTO);
        RestaurantPauseQueryResult result = createRestaurantPauseUseCase.execute(command);
        RestaurantPauseResponseDTO response = dtoMapper.toResponseDto(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurantPause(@PathVariable Long id) {
        softDeleteRestaurantPauseUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restoreRestaurantPause(@PathVariable Long id) {
        restoreRestaurantPauseUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
