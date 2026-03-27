package com.ute.foodiedash.interfaces.rest.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAddressResponseDTO {
    private Long id;
    private String label;
    private String address;
    private BigDecimal lat;
    private BigDecimal lng;
    private String receiverName;
    private String receiverPhone;
    private String note;
    @JsonProperty("isDefault")
    private boolean defaultAddress;
}

