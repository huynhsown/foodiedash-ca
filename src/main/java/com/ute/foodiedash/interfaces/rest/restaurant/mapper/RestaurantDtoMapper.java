package com.ute.foodiedash.interfaces.rest.restaurant.mapper;

import com.ute.foodiedash.application.restaurant.command.CreateRestaurantCommand;
import com.ute.foodiedash.application.restaurant.query.RestaurantDetailQueryResult;
import com.ute.foodiedash.application.restaurant.query.RestaurantQueryResult;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.CreateRestaurantDTO;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.RestaurantDetailDTO;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.RestaurantResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantDtoMapper {
    CreateRestaurantCommand toCommand(CreateRestaurantDTO dto);
    RestaurantResponseDTO toResponseDto(RestaurantQueryResult result);
    RestaurantDetailDTO toDetailDto(RestaurantDetailQueryResult result);
}
