package com.ute.foodiedash.interfaces.rest.merchant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MerchantRestaurantDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String status;
    private List<BusinessHourDTO> businessHours;
}
