package com.ute.foodiedash.interfaces.rest.driver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDriverBankDTO {

    @NotBlank(message = "Bank name is required")
    @Size(max = 100, message = "Bank name must not exceed 100 characters")
    private String bankName;

    @NotBlank(message = "Bank account number is required")
    @Size(max = 50, message = "Bank account number must not exceed 50 characters")
    private String bankAccount;

    @NotBlank(message = "Bank holder name is required")
    @Size(max = 255, message = "Bank holder name must not exceed 255 characters")
    private String bankHolderName;
}
