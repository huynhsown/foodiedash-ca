package com.ute.foodiedash.application.restaurant.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDetailQueryResult {
    private Long id;
    private String name;
    private String slug;
    private Double ratingAvg;
    private boolean isOpen;
    private List<String> categories;
    private Double distance;
    private Integer eta;
    private String imageUrl;
    private String address;
    private BigDecimal lat;
    private BigDecimal lng;
}
