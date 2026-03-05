package com.ute.foodiedash.interfaces.rest.restaurant.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RestaurantResponseDTO {

    private Long id;
    private String code;
    private String slug;
    private String name;
    private String description;
    private String address;
    private String phone;
    private BigDecimal lat;
    private BigDecimal lng;
    private String status;
}
