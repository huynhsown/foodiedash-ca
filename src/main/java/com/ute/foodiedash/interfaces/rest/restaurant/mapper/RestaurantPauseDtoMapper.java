package com.ute.foodiedash.interfaces.rest.restaurant.mapper;

import com.ute.foodiedash.application.restaurant.command.CreateRestaurantPauseCommand;
import com.ute.foodiedash.application.restaurant.query.RestaurantPauseQueryResult;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.CreateRestaurantPauseDTO;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.RestaurantPauseResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantPauseDtoMapper {
    CreateRestaurantPauseCommand toCommand(CreateRestaurantPauseDTO dto);
    RestaurantPauseResponseDTO toResponseDto(RestaurantPauseQueryResult result);
}
