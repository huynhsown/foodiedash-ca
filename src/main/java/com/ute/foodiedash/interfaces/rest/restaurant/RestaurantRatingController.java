package com.ute.foodiedash.interfaces.rest.restaurant;

import com.ute.foodiedash.application.restaurant.command.CreateRestaurantRatingCommand;
import com.ute.foodiedash.application.restaurant.query.RestaurantRatingQueryResult;
import com.ute.foodiedash.application.restaurant.usecase.CreateRestaurantRatingUseCase;
import com.ute.foodiedash.application.restaurant.usecase.RestoreRestaurantRatingUseCase;
import com.ute.foodiedash.application.restaurant.usecase.SoftDeleteRestaurantRatingUseCase;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.CreateRestaurantRatingDTO;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.RestaurantRatingResponseDTO;
import com.ute.foodiedash.interfaces.rest.restaurant.mapper.RestaurantRatingDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/restaurant-ratings")
@RequiredArgsConstructor
public class RestaurantRatingController {

    private final CreateRestaurantRatingUseCase createRestaurantRatingUseCase;
    private final SoftDeleteRestaurantRatingUseCase softDeleteRestaurantRatingUseCase;
    private final RestoreRestaurantRatingUseCase restoreRestaurantRatingUseCase;
    private final RestaurantRatingDtoMapper dtoMapper;

    @PostMapping
    public ResponseEntity<RestaurantRatingResponseDTO> createRestaurantRating(
            @Valid @RequestBody CreateRestaurantRatingDTO createRestaurantRatingDTO) {
        CreateRestaurantRatingCommand command = dtoMapper.toCommand(createRestaurantRatingDTO);
        RestaurantRatingQueryResult result = createRestaurantRatingUseCase.execute(command);
        RestaurantRatingResponseDTO response = dtoMapper.toResponseDto(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurantRating(@PathVariable Long id) {
        softDeleteRestaurantRatingUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restoreRestaurantRating(@PathVariable Long id) {
        restoreRestaurantRatingUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
