package com.ute.foodiedash.interfaces.rest.merchant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessHourDTO {
    private Integer dayOfWeek;
    private String openTime;
    private String closeTime;
}
