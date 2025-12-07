package com.ute.foodiedash.interfaces.rest.auth.mapper;

import com.ute.foodiedash.application.user.command.LoginCommand;
import com.ute.foodiedash.application.user.query.LoginQueryResult;
import com.ute.foodiedash.interfaces.rest.auth.dto.LoginDTO;
import com.ute.foodiedash.interfaces.rest.auth.dto.LoginResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthDtoMapper {
    LoginCommand toCommand(LoginDTO dto);
    LoginResponseDTO toResponseDto(LoginQueryResult result);
}
