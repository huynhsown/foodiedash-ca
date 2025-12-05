package com.ute.foodiedash.infrastructure.search.meilisearch.docs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantSearchDocument {
    private Long id;
    private String name;
    private String slug;

    @JsonProperty("_geo")
    private GeoPoint geo;

    private Double ratingAvg;
    private Integer ratingCount;

    private Integer prepTimeAvg;

    private List<String> categories;
    private List<String> menuItems;

    @Data
    @AllArgsConstructor
    public static class GeoPoint {
        private double lat;
        private double lng;
    }
}
