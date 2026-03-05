package com.ute.foodiedash.interfaces.rest.restaurant;

import com.ute.foodiedash.application.restaurant.command.CreateRestaurantCategoryMapCommand;
import com.ute.foodiedash.application.restaurant.query.RestaurantCategoryMapQueryResult;
import com.ute.foodiedash.application.restaurant.usecase.CreateRestaurantCategoryMapUseCase;
import com.ute.foodiedash.application.restaurant.usecase.RestoreRestaurantCategoryMapUseCase;
import com.ute.foodiedash.application.restaurant.usecase.SoftDeleteRestaurantCategoryMapUseCase;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.CreateRestaurantCategoryMapDTO;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.RestaurantCategoryMapResponseDTO;
import com.ute.foodiedash.interfaces.rest.restaurant.mapper.RestaurantCategoryMapDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/restaurant-category-maps")
@RequiredArgsConstructor
public class RestaurantCategoryMapController {

    private final CreateRestaurantCategoryMapUseCase createRestaurantCategoryMapUseCase;
    private final SoftDeleteRestaurantCategoryMapUseCase softDeleteRestaurantCategoryMapUseCase;
    private final RestoreRestaurantCategoryMapUseCase restoreRestaurantCategoryMapUseCase;
    private final RestaurantCategoryMapDtoMapper dtoMapper;

    @PostMapping
    public ResponseEntity<RestaurantCategoryMapResponseDTO> createRestaurantCategoryMap(
            @Valid @RequestBody CreateRestaurantCategoryMapDTO createRestaurantCategoryMapDTO) {
        CreateRestaurantCategoryMapCommand command = dtoMapper.toCommand(createRestaurantCategoryMapDTO);
        RestaurantCategoryMapQueryResult result = createRestaurantCategoryMapUseCase.execute(command);
        RestaurantCategoryMapResponseDTO response = dtoMapper.toResponseDto(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurantCategoryMap(@PathVariable Long id) {
        softDeleteRestaurantCategoryMapUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restoreRestaurantCategoryMap(@PathVariable Long id) {
        restoreRestaurantCategoryMapUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
