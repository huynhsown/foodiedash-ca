package com.ute.foodiedash.infrastructure.search.meilisearch.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RestaurantSearchHitDTO {
    private Long id;
    private String name;
    private String slug;
    private Double ratingAvg;
    private boolean isOpen;
    private List<String> categories;
    private Double distance;
    private Integer eta;
}
