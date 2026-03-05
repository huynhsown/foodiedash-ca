package com.ute.foodiedash.interfaces.rest.restaurant.mapper;

import com.ute.foodiedash.application.restaurant.command.CreateRestaurantBusinessHourCommand;
import com.ute.foodiedash.application.restaurant.query.RestaurantBusinessHourQueryResult;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.CreateRestaurantBusinessHourDTO;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.RestaurantBusinessHourResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantBusinessHourDtoMapper {
    CreateRestaurantBusinessHourCommand toCommand(CreateRestaurantBusinessHourDTO dto);
    RestaurantBusinessHourResponseDTO toResponseDto(RestaurantBusinessHourQueryResult result);
}
