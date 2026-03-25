package com.ute.foodiedash.interfaces.rest.driver.mapper;

import com.ute.foodiedash.application.user.command.RegisterDriverCommand;
import com.ute.foodiedash.application.user.command.UpdateDriverIdentityCommand;
import com.ute.foodiedash.application.user.query.UserQueryResult;
import com.ute.foodiedash.interfaces.rest.driver.dto.RegisterDriverDTO;
import com.ute.foodiedash.interfaces.rest.driver.dto.UpdateDriverIdentityDTO;
import com.ute.foodiedash.interfaces.rest.user.dto.UserResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DriverDtoMapper {

    RegisterDriverCommand toCommand(RegisterDriverDTO dto);

    UpdateDriverIdentityCommand toCommand(UpdateDriverIdentityDTO dto);

    UserResponseDTO toResponseDto(UserQueryResult result);
}
