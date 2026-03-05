package com.ute.foodiedash.interfaces.rest.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class RestaurantDetailDTO {
    private Long id;
    private String name;
    private String slug;
    private Double ratingAvg;
    @JsonProperty("isOpen")
    private boolean isOpen;
    private List<String> categories;
    private Double distance;
    private Integer eta;
    private String imageUrl;
    private String address;
    private BigDecimal lat;
    private BigDecimal lng;
}
