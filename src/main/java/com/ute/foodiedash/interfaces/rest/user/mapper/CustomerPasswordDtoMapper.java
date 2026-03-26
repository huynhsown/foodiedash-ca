package com.ute.foodiedash.interfaces.rest.user.mapper;

import com.ute.foodiedash.application.user.command.ChangeCustomerPasswordCommand;
import com.ute.foodiedash.interfaces.rest.user.dto.ChangeCustomerPasswordDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerPasswordDtoMapper {
    ChangeCustomerPasswordCommand toCommand(ChangeCustomerPasswordDTO dto);
}

