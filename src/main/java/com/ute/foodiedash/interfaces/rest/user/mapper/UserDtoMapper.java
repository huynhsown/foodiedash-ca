package com.ute.foodiedash.interfaces.rest.user.mapper;

import com.ute.foodiedash.application.user.command.RegisterCustomerCommand;
import com.ute.foodiedash.application.user.query.UserQueryResult;
import com.ute.foodiedash.interfaces.rest.user.dto.RegisterCustomerDTO;
import com.ute.foodiedash.interfaces.rest.user.dto.UserResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {
    RegisterCustomerCommand toCommand(RegisterCustomerDTO dto);
    UserResponseDTO toResponseDto(UserQueryResult result);
}
