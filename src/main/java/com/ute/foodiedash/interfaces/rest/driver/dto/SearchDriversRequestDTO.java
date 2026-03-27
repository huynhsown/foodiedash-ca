package com.ute.foodiedash.interfaces.rest.driver.dto;

import com.ute.foodiedash.domain.user.enums.DriverVerificationStatus;
import com.ute.foodiedash.domain.user.enums.UserStatus;
import com.ute.foodiedash.domain.user.enums.VehicleType;
import com.ute.foodiedash.interfaces.rest.common.dto.PageRequestDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class SearchDriversRequestDTO extends PageRequestDTO {
    private String keyword;
    private UserStatus userStatus;
    private DriverVerificationStatus driverVerificationStatus;
    private VehicleType vehicleType;
    private Instant createdFrom;
    private Instant createdTo;
}
