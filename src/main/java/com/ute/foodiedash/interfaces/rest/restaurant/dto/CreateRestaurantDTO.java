package com.ute.foodiedash.interfaces.rest.restaurant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateRestaurantDTO {

    @NotBlank(message = "Code is required")
    @Size(max = 25, message = "Code must not exceed 25 characters")
    private String code;

    @Size(max = 255, message = "Slug must not exceed 255 characters")
    private String slug;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    private String description;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;

    @NotBlank(message = "Phone is required")
    @Size(max = 255, message = "Phone must not exceed 255 characters")
    private String phone;

    @NotNull(message = "Latitude is required")
    private BigDecimal lat;

    @NotNull(message = "Longitude is required")
    private BigDecimal lng;

    @NotBlank(message = "Status is required")
    @Size(max = 255, message = "Status must not exceed 255 characters")
    private String status;
}
