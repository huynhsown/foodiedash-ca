package com.ute.foodiedash.interfaces.rest.driver.mapper;

import com.ute.foodiedash.application.user.query.DriverProfileQueryResult;
import com.ute.foodiedash.interfaces.rest.driver.dto.DriverProfileResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DriverProfileDtoMapper {

    DriverProfileResponseDTO toResponseDto(DriverProfileQueryResult result);
}
