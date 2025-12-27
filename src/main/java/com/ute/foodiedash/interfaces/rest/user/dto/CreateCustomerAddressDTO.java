package com.ute.foodiedash.interfaces.rest.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
public class CreateCustomerAddressDTO {

    @NotBlank(message = "Label is required")
    @Size(max = 50, message = "Label must not exceed 50 characters")
    private String label;

    @NotBlank(message = "Address is required")
    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    @NotNull(message = "Latitude is required")
    private BigDecimal lat;

    @NotNull(message = "Longitude is required")
    private BigDecimal lng;

    @NotBlank(message = "Receiver name is required")
    @Size(max = 255, message = "Receiver name must not exceed 255 characters")
    private String receiverName;

    @NotBlank(message = "Receiver phone is required")
    @Size(max = 20, message = "Receiver phone must not exceed 20 characters")
    private String receiverPhone;

    @Size(max = 500, message = "Note must not exceed 500 characters")
    private String note;

    @JsonProperty("isDefault")
    private boolean defaultAddress;
}

