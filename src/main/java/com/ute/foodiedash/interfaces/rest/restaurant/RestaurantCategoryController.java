package com.ute.foodiedash.interfaces.rest.restaurant;

import com.ute.foodiedash.application.restaurant.command.CreateRestaurantCategoryCommand;
import com.ute.foodiedash.application.restaurant.query.RestaurantCategoryQueryResult;
import com.ute.foodiedash.application.restaurant.usecase.CreateRestaurantCategoryUseCase;
import com.ute.foodiedash.application.restaurant.usecase.GetAllRestaurantCategoriesUseCase;
import com.ute.foodiedash.application.restaurant.usecase.GetRestaurantCategoriesUseCase;
import com.ute.foodiedash.application.restaurant.usecase.RestoreRestaurantCategoryUseCase;
import com.ute.foodiedash.application.restaurant.usecase.SoftDeleteRestaurantCategoryUseCase;
import com.ute.foodiedash.interfaces.rest.common.dto.PageInfo;
import com.ute.foodiedash.interfaces.rest.common.dto.PageRequestDTO;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.CreateRestaurantCategoryDTO;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.RestaurantCategoryResponseDTO;
import com.ute.foodiedash.interfaces.rest.restaurant.mapper.RestaurantCategoryDtoMapper;
import com.ute.foodiedash.interfaces.rest.restaurant.mapper.RestaurantCategoryPageMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurant-categories")
@RequiredArgsConstructor
public class RestaurantCategoryController {

    private final CreateRestaurantCategoryUseCase createRestaurantCategoryUseCase;
    private final GetAllRestaurantCategoriesUseCase getAllRestaurantCategoriesUseCase;
    private final GetRestaurantCategoriesUseCase getRestaurantCategoriesUseCase;
    private final SoftDeleteRestaurantCategoryUseCase softDeleteRestaurantCategoryUseCase;
    private final RestoreRestaurantCategoryUseCase restoreRestaurantCategoryUseCase;
    private final RestaurantCategoryDtoMapper dtoMapper;
    private final RestaurantCategoryPageMapper pageMapper;

    @PostMapping
    public ResponseEntity<RestaurantCategoryResponseDTO> createRestaurantCategory(
            @Valid @RequestBody CreateRestaurantCategoryDTO createRestaurantCategoryDTO) {
        CreateRestaurantCategoryCommand command = dtoMapper.toCommand(createRestaurantCategoryDTO);
        RestaurantCategoryQueryResult result = createRestaurantCategoryUseCase.execute(command);
        RestaurantCategoryResponseDTO response = dtoMapper.toResponseDto(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<RestaurantCategoryResponseDTO>> getAllCategories() {
        List<RestaurantCategoryResponseDTO> categories = getAllRestaurantCategoriesUseCase.execute().stream()
            .map(dtoMapper::toResponseDto)
            .toList();
        return ResponseEntity.ok(categories);
    }

    @GetMapping
    public ResponseEntity<PageInfo<RestaurantCategoryResponseDTO>> getCategories(
            PageRequestDTO pageRequestDTO) {
        var query = pageMapper.toQuery(pageRequestDTO);
        var result = getRestaurantCategoriesUseCase.execute(query);
        PageInfo<RestaurantCategoryResponseDTO> pageInfo = pageMapper.toPageInfo(result);
        return ResponseEntity.ok(pageInfo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurantCategory(@PathVariable Long id) {
        softDeleteRestaurantCategoryUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restoreRestaurantCategory(@PathVariable Long id) {
        restoreRestaurantCategoryUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
