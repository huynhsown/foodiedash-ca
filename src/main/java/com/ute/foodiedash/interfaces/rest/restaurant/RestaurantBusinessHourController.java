package com.ute.foodiedash.interfaces.rest.restaurant;

import com.ute.foodiedash.application.restaurant.command.CreateRestaurantBusinessHourCommand;
import com.ute.foodiedash.application.restaurant.query.RestaurantBusinessHourQueryResult;
import com.ute.foodiedash.application.restaurant.usecase.CreateRestaurantBusinessHourUseCase;
import com.ute.foodiedash.application.restaurant.usecase.RestoreRestaurantBusinessHourUseCase;
import com.ute.foodiedash.application.restaurant.usecase.SoftDeleteRestaurantBusinessHourUseCase;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.CreateRestaurantBusinessHourDTO;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.RestaurantBusinessHourResponseDTO;
import com.ute.foodiedash.interfaces.rest.restaurant.mapper.RestaurantBusinessHourDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/restaurant-business-hours")
@RequiredArgsConstructor
public class RestaurantBusinessHourController {

    private final CreateRestaurantBusinessHourUseCase createRestaurantBusinessHourUseCase;
    private final SoftDeleteRestaurantBusinessHourUseCase softDeleteRestaurantBusinessHourUseCase;
    private final RestoreRestaurantBusinessHourUseCase restoreRestaurantBusinessHourUseCase;
    private final RestaurantBusinessHourDtoMapper dtoMapper;

    @PostMapping
    public ResponseEntity<RestaurantBusinessHourResponseDTO> createRestaurantBusinessHour(
            @Valid @RequestBody CreateRestaurantBusinessHourDTO createRestaurantBusinessHourDTO) {
        CreateRestaurantBusinessHourCommand command = dtoMapper.toCommand(createRestaurantBusinessHourDTO);
        RestaurantBusinessHourQueryResult result = createRestaurantBusinessHourUseCase.execute(command);
        RestaurantBusinessHourResponseDTO response = dtoMapper.toResponseDto(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurantBusinessHour(@PathVariable Long id) {
        softDeleteRestaurantBusinessHourUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restoreRestaurantBusinessHour(@PathVariable Long id) {
        restoreRestaurantBusinessHourUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
