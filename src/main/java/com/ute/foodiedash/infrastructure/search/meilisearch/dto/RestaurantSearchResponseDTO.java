package com.ute.foodiedash.infrastructure.search.meilisearch.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantSearchResponseDTO {
    private List<RestaurantSearchHitDTO> hits;
    private Integer limit;
    private Integer offset;
    private Long estimatedTotalHits;
    private Integer processingTimeMs;
    private String query;
}
