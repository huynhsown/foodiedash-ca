package com.ute.foodiedash.interfaces.rest.driver.mapper;

import com.ute.foodiedash.application.user.command.RegisterDriverCommand;
import com.ute.foodiedash.application.user.command.SearchDriversCommand;
import com.ute.foodiedash.application.user.command.UpdateDriverBankCommand;
import com.ute.foodiedash.application.user.command.UpdateDriverIdentityCommand;
import com.ute.foodiedash.application.user.command.UpdateDriverLicenseCommand;
import com.ute.foodiedash.application.user.command.UpdateDriverVehicleCommand;
import com.ute.foodiedash.application.user.query.SearchDriverQueryResult;
import com.ute.foodiedash.application.user.query.UserQueryResult;
import com.ute.foodiedash.interfaces.rest.driver.dto.RegisterDriverDTO;
import com.ute.foodiedash.interfaces.rest.driver.dto.SearchDriverResponseDTO;
import com.ute.foodiedash.interfaces.rest.driver.dto.SearchDriversRequestDTO;
import com.ute.foodiedash.interfaces.rest.driver.dto.UpdateDriverBankDTO;
import com.ute.foodiedash.interfaces.rest.driver.dto.UpdateDriverIdentityDTO;
import com.ute.foodiedash.interfaces.rest.driver.dto.UpdateDriverLicenseDTO;
import com.ute.foodiedash.interfaces.rest.driver.dto.UpdateDriverVehicleDTO;
import com.ute.foodiedash.interfaces.rest.user.dto.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DriverDtoMapper {

    RegisterDriverCommand toCommand(RegisterDriverDTO dto);

    UserResponseDTO toResponseDto(UserQueryResult result);

    UpdateDriverIdentityCommand toCommand(UpdateDriverIdentityDTO dto);

    UpdateDriverLicenseCommand toCommand(UpdateDriverLicenseDTO dto);

    UpdateDriverVehicleCommand toCommand(UpdateDriverVehicleDTO dto);

    UpdateDriverBankCommand toCommand(UpdateDriverBankDTO dto);

    @Mapping(target = "sortDirection", expression = "java(dto.isAscending() ? \"asc\" : \"desc\")")
    SearchDriversCommand toCommand(SearchDriversRequestDTO dto);

    SearchDriverResponseDTO toResponseDto(SearchDriverQueryResult result);
}
