package com.ute.foodiedash.interfaces.rest.restaurant.mapper;

import com.ute.foodiedash.application.restaurant.command.CreateRestaurantPreparationSettingCommand;
import com.ute.foodiedash.application.restaurant.query.RestaurantPreparationSettingQueryResult;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.CreateRestaurantPreparationSettingDTO;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.RestaurantPreparationSettingResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantPreparationSettingDtoMapper {
    CreateRestaurantPreparationSettingCommand toCommand(CreateRestaurantPreparationSettingDTO dto);
    RestaurantPreparationSettingResponseDTO toResponseDto(RestaurantPreparationSettingQueryResult result);
}
