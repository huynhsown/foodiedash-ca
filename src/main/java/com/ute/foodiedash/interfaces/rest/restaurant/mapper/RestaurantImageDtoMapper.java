package com.ute.foodiedash.interfaces.rest.restaurant.mapper;

import com.ute.foodiedash.application.restaurant.command.CreateRestaurantImageCommand;
import com.ute.foodiedash.application.restaurant.query.RestaurantImageQueryResult;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.CreateRestaurantImageDTO;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.RestaurantImageResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantImageDtoMapper {
    CreateRestaurantImageCommand toCommand(CreateRestaurantImageDTO dto);
    RestaurantImageResponseDTO toResponseDto(RestaurantImageQueryResult result);
}
