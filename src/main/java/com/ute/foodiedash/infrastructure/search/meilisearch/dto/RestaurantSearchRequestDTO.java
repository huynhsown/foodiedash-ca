package com.ute.foodiedash.infrastructure.search.meilisearch.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RestaurantSearchRequestDTO {
    private String query;
    private String categoryName;
    private BigDecimal lat;
    private BigDecimal lng;
    private Integer radius; // km
    private Integer limit = 20;
    private Integer offset = 0;
    private Boolean isOpen;
    private Double minRating;
}
