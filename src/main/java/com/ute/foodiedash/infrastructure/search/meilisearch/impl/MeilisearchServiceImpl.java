package com.ute.foodiedash.infrastructure.search.meilisearch.impl;

import com.ute.foodiedash.domain.restaurant.model.Restaurant;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRepository;
import com.ute.foodiedash.infrastructure.search.meilisearch.MeilisearchService;
import com.ute.foodiedash.infrastructure.search.meilisearch.RestaurantSearchDocumentService;
import com.ute.foodiedash.infrastructure.search.meilisearch.docs.RestaurantSearchDocument;
import com.ute.foodiedash.infrastructure.search.meilisearch.dto.RestaurantSearchHitDTO;
import com.ute.foodiedash.infrastructure.search.meilisearch.dto.RestaurantSearchRequestDTO;
import com.ute.foodiedash.infrastructure.search.meilisearch.dto.RestaurantSearchResponseDTO;
import com.ute.foodiedash.infrastructure.util.RestaurantUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.model.SearchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "search.engine", havingValue = "meilisearch")
public class MeilisearchServiceImpl implements MeilisearchService {

    private final Client meiliClient;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantUtils restaurantUtils;
    private final RestaurantSearchDocumentService restaurantSearchDocumentService;

    @Value("${meilisearch.index.restaurants}")
    private String indexName;

    @Override
    public void initializeIndex() {
        try {
            Index index = meiliClient.index(indexName);

            String[] searchableAttributes = {"name", "slug", "categories", "menuItems"};
            index.updateSearchableAttributesSettings(searchableAttributes);

            String[] filterableAttributes = {"slug", "categories", "isOpen", "ratingAvg", "_geo"};
            index.updateFilterableAttributesSettings(filterableAttributes);

            String[] sortableAttributes = {"name", "ratingAvg", "ratingCount", "prepTimeAvg"};
            index.updateSortableAttributesSettings(sortableAttributes);

            log.info("Meilisearch index '{}' initialized successfully", indexName);
        } catch (MeilisearchException e) {
            log.error("Error initializing Meilisearch index", e);
            throw new RuntimeException("Failed to initialize Meilisearch index", e);
        }
    }

    @Override
    public void indexRestaurant(RestaurantSearchDocument document) {
        try {
            Index index = meiliClient.index(indexName);

            Map<String, Object> docMap = convertToMap(document);
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(List.of(docMap));
            index.addDocuments(jsonString);

            log.debug("Restaurant {} indexed successfully", document.getId());
        } catch (Exception e) {
            log.error("Error indexing restaurant {}", document.getId(), e);
            throw new RuntimeException("Failed to index restaurant", e);
        }
    }

    @Override
    public void indexRestaurants(List<RestaurantSearchDocument> documents) {
        try {
            Index index = meiliClient.index(indexName);

            List<Map<String, Object>> docMaps = documents.stream()
                .map(this::convertToMap)
                .toList();

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(docMaps);
            index.addDocuments(jsonString);

            log.info("Indexed {} restaurants successfully", documents.size());
        } catch (Exception e) {
            log.error("Error indexing restaurants", e);
            throw new RuntimeException("Failed to index restaurants", e);
        }
    }

    @Override
    public void deleteRestaurant(Long restaurantId) {
        try {
            Index index = meiliClient.index(indexName);
            index.deleteDocument(String.valueOf(restaurantId));
            log.debug("Restaurant {} deleted from index", restaurantId);
        } catch (MeilisearchException e) {
            log.error("Error deleting restaurant {} from index", restaurantId, e);
            throw new RuntimeException("Failed to delete restaurant from index", e);
        }
    }

    @Override
    public void updateRestaurant(RestaurantSearchDocument document) {
        indexRestaurant(document);
    }

    @Override
    public RestaurantSearchResponseDTO searchRestaurants(RestaurantSearchRequestDTO request) {
        try {
            Index index = meiliClient.index(indexName);

            String query = request.getQuery() != null ? request.getQuery() : "";

            List<String> filters = new ArrayList<>();
            if (request.getCategoryName() != null && !request.getCategoryName().isEmpty()) {
                filters.add("categories = \"" + request.getCategoryName() + "\"");
            }
            if (request.getMinRating() != null) {
                filters.add("ratingAvg >= " + request.getMinRating());
            }

            if (request.getLat() != null && request.getLng() != null) {
                int radius = request.getRadius() != null
                        ? request.getRadius() * 1000
                        : 10000;

                String geoFilter = String.format(
                        "_geoRadius(%s, %s, %d)",
                        request.getLat(),
                        request.getLng(),
                        radius
                );

                filters.add(geoFilter);
            }

            SearchRequest searchRequest = new SearchRequest(query);
            searchRequest.setLimit(request.getLimit());
            searchRequest.setOffset(request.getOffset());
            if (!filters.isEmpty()) {
                searchRequest.setFilter(filters.toArray(new String[0]));
            }

            com.meilisearch.sdk.model.Searchable searchable = index.search(searchRequest);
            SearchResult searchResult = (SearchResult) searchable;

            RestaurantSearchResponseDTO response = new RestaurantSearchResponseDTO();
            response.setQuery(query);
            response.setLimit(request.getLimit());
            response.setOffset(request.getOffset());
            response.setEstimatedTotalHits((long) searchResult.getEstimatedTotalHits());
            response.setProcessingTimeMs(searchResult.getProcessingTimeMs());

            List<RestaurantSearchDocument> docs = new ArrayList<>();
            for (Object hit : searchResult.getHits()) {
                Map<String, Object> hitMap = (Map<String, Object>) hit;
                RestaurantSearchDocument document = convertFromMap(hitMap);
                docs.add(document);
            }

            List<Long> ids = docs.stream()
                    .map(RestaurantSearchDocument::getId)
                    .toList();

            List<Restaurant> restaurants = new ArrayList<>();
            for (Long id : ids) {
                restaurantRepository.findById(id).ifPresent(restaurants::add);
            }
            
            Map<Long, Restaurant> restaurantById = new HashMap<>();
            for (Restaurant r : restaurants) {
                restaurantById.put(r.getId(), r);
            }

            List<RestaurantSearchHitDTO> hits = new ArrayList<>();
            for (RestaurantSearchDocument doc : docs) {
                Restaurant restaurant = restaurantById.get(doc.getId());
                if (restaurant == null) {
                    continue;
                }

                RestaurantSearchHitDTO hitDTO = new RestaurantSearchHitDTO();
                hitDTO.setId(restaurant.getId());
                hitDTO.setName(restaurant.getName());
                hitDTO.setSlug(restaurant.getSlug());
                hitDTO.setCategories(doc.getCategories());

                if (restaurantUtils.checkIfRestaurantIsOpen(restaurant.getId())) {
                    hitDTO.setRatingAvg(doc.getRatingAvg());

                    Double distanceKm = RestaurantUtils.calculateDistanceKm(request.getLat(), request.getLng(), doc);
                    hitDTO.setDistance(distanceKm);

                    Integer eta = RestaurantUtils.calculateEtaMinutes(distanceKm, doc.getPrepTimeAvg());
                    hitDTO.setEta(eta);
                    hitDTO.setOpen(true);
                } else {
                    hitDTO.setOpen(false);
                }

                hits.add(hitDTO);
            }

            response.setHits(hits);

            return response;
        } catch (MeilisearchException e) {
            log.error("Error searching restaurants", e);
            throw new RuntimeException("Failed to search restaurants", e);
        }
    }

    private Map<String, Object> convertToMap(RestaurantSearchDocument document) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", document.getId());
        map.put("name", document.getName());
        map.put("slug", document.getSlug());

        if (document.getGeo() != null) {
            Map<String, Object> geo = new HashMap<>();
            geo.put("lat", document.getGeo().getLat());
            geo.put("lng", document.getGeo().getLng());
            map.put("_geo", geo);
        }

        map.put("ratingAvg", document.getRatingAvg());
        map.put("ratingCount", document.getRatingCount());
        map.put("prepTimeAvg", document.getPrepTimeAvg());
        map.put("categories", document.getCategories());
        map.put("menuItems", document.getMenuItems());

        return map;
    }

    private RestaurantSearchDocument convertFromMap(Map<String, Object> map) {
        RestaurantSearchDocument document = new RestaurantSearchDocument();
        if (map.get("id") != null) {
            if (map.get("id") instanceof Number) {
                document.setId(((Number) map.get("id")).longValue());
            } else {
                document.setId(Long.valueOf(map.get("id").toString()));
            }
        }
        document.setName((String) map.get("name"));
        document.setSlug((String) map.get("slug"));

        if (map.get("_geo") != null) {
            Map<String, Object> geoMap = (Map<String, Object>) map.get("_geo");
            RestaurantSearchDocument.GeoPoint geo = new RestaurantSearchDocument.GeoPoint(
                ((Number) geoMap.get("lat")).doubleValue(),
                ((Number) geoMap.get("lng")).doubleValue()
            );
            document.setGeo(geo);
        }

        if (map.get("ratingAvg") != null) {
            document.setRatingAvg(((Number) map.get("ratingAvg")).doubleValue());
        }
        if (map.get("ratingCount") != null) {
            document.setRatingCount(((Number) map.get("ratingCount")).intValue());
        }
        if (map.get("prepTimeAvg") != null) {
            document.setPrepTimeAvg(((Number) map.get("prepTimeAvg")).intValue());
        }
        if (map.get("categories") != null) {
            document.setCategories((List<String>) map.get("categories"));
        }
        if (map.get("menuItems") != null) {
            document.setMenuItems((List<String>) map.get("menuItems"));
        }

        return document;
    }
}
