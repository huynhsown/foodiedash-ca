package com.ute.foodiedash.interfaces.rest.restaurant;

import com.ute.foodiedash.application.restaurant.command.CreateRestaurantCommand;
import com.ute.foodiedash.application.restaurant.query.RestaurantQueryResult;
import com.ute.foodiedash.application.restaurant.usecase.CreateRestaurantUseCase;
import com.ute.foodiedash.application.restaurant.usecase.GetRestaurantBySlugUseCase;
import com.ute.foodiedash.application.restaurant.usecase.GetRestaurantDetailBySlugUseCase;
import com.ute.foodiedash.application.restaurant.usecase.GetRestaurantSnapshotByIdUseCase;
import com.ute.foodiedash.application.restaurant.usecase.RestoreRestaurantUseCase;
import com.ute.foodiedash.application.restaurant.usecase.SoftDeleteRestaurantUseCase;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.CreateRestaurantDTO;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.RestaurantDetailDTO;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.RestaurantResponseDTO;
import com.ute.foodiedash.interfaces.rest.restaurant.mapper.RestaurantDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final CreateRestaurantUseCase createRestaurantUseCase;
    private final GetRestaurantBySlugUseCase getRestaurantBySlugUseCase;
    private final GetRestaurantDetailBySlugUseCase getRestaurantDetailBySlugUseCase;
    private final GetRestaurantSnapshotByIdUseCase getRestaurantSnapshotByIdUseCase;
    private final SoftDeleteRestaurantUseCase softDeleteRestaurantUseCase;
    private final RestoreRestaurantUseCase restoreRestaurantUseCase;
    private final RestaurantDtoMapper dtoMapper;

    @PostMapping
    public ResponseEntity<RestaurantResponseDTO> createRestaurant(
            @Valid @RequestBody CreateRestaurantDTO dto) {
        CreateRestaurantCommand command = dtoMapper.toCommand(dto);
        RestaurantQueryResult result = createRestaurantUseCase.execute(command);
        RestaurantResponseDTO response = dtoMapper.toResponseDto(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<RestaurantDetailDTO> getRestaurantBySlug(
            @PathVariable String slug,
            @RequestParam(required = false) BigDecimal lat,
            @RequestParam(required = false) BigDecimal lng) {
        RestaurantDetailDTO restaurantDetailDTO = dtoMapper.toDetailDto(
            getRestaurantDetailBySlugUseCase.execute(slug, lat, lng));
        return ResponseEntity.ok(restaurantDetailDTO);
    }

    @GetMapping("/{id}/snapshot")
    public ResponseEntity<RestaurantDetailDTO> getRestaurantById(
            @PathVariable Long id,
            @RequestParam(required = false) BigDecimal lat,
            @RequestParam(required = false) BigDecimal lng) {
        RestaurantDetailDTO restaurantDetailDTO = dtoMapper.toDetailDto(
            getRestaurantSnapshotByIdUseCase.execute(id, lat, lng));
        return ResponseEntity.ok(restaurantDetailDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        softDeleteRestaurantUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restoreRestaurant(@PathVariable Long id) {
        restoreRestaurantUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
