package com.ute.foodiedash.interfaces.rest.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummariesResponseDTO {
    private List<OrderSummaryResponseDTO> orders;
}

