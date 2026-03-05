package com.ute.foodiedash.interfaces.rest.menu;

import com.ute.foodiedash.application.menu.query.RestaurantMenuQueryResult;
import com.ute.foodiedash.application.menu.usecase.GetRestaurantMenuUseCase;
import com.ute.foodiedash.interfaces.rest.menu.dto.RestaurantMenuResponseDTO;
import com.ute.foodiedash.interfaces.rest.menu.mapper.RestaurantMenuDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantMenuController {

    private final GetRestaurantMenuUseCase getRestaurantMenuUseCase;
    private final RestaurantMenuDtoMapper dtoMapper;

    @GetMapping("/{restaurantId}/menus")
    public ResponseEntity<RestaurantMenuResponseDTO> getMenuByRestaurantId(@PathVariable Long restaurantId) {
        RestaurantMenuQueryResult result = getRestaurantMenuUseCase.execute(restaurantId);
        RestaurantMenuResponseDTO response = dtoMapper.toResponseDto(result);
        return ResponseEntity.ok(response);
    }
}
