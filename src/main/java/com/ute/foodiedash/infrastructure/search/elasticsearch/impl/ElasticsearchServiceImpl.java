package com.ute.foodiedash.infrastructure.search.elasticsearch.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.json.JsonData;
import com.ute.foodiedash.domain.restaurant.model.Restaurant;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRepository;
import com.ute.foodiedash.infrastructure.search.meilisearch.MeilisearchService;
import com.ute.foodiedash.infrastructure.search.meilisearch.docs.MenuItemSearchDocument;
import com.ute.foodiedash.infrastructure.search.meilisearch.RestaurantSearchDocumentService;
import com.ute.foodiedash.infrastructure.search.meilisearch.docs.RestaurantSearchDocument;
import com.ute.foodiedash.infrastructure.search.meilisearch.dto.RestaurantSearchHitDTO;
import com.ute.foodiedash.infrastructure.search.meilisearch.dto.RestaurantSearchRequestDTO;
import com.ute.foodiedash.infrastructure.search.meilisearch.dto.RestaurantSearchResponseDTO;
import com.ute.foodiedash.infrastructure.util.RestaurantUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "search.engine", havingValue = "elasticsearch")
public class ElasticsearchServiceImpl implements MeilisearchService {

    private static final int DEFAULT_RADIUS_METERS = 10_000;

    private final ElasticsearchClient elasticsearchClient;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantUtils restaurantUtils;
    private final RestaurantSearchDocumentService restaurantSearchDocumentService;

    @Value("${elasticsearch.index.restaurants}")
    private String restaurantsIndex;

    @Value("${elasticsearch.index.menu-items:menu-items}")
    private String menuItemsIndex;

    @Value("${elasticsearch.menu-items.embedding-dims:3072}")
    private int menuItemEmbeddingDims;

    @Override
    public void initializeIndex() {
        try {
            ensureIndexExists(restaurantsIndex, restaurantsMapping());
            ensureIndexExists(menuItemsIndex, menuItemsMapping());
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Elasticsearch index", e);
        }
    }

    @Override
    public void indexRestaurant(RestaurantSearchDocument document) {
        upsertRestaurant(document);
    }

    @Override
    public void indexRestaurants(List<RestaurantSearchDocument> documents) {
        for (RestaurantSearchDocument document : documents) {
            upsertRestaurant(document);
        }
    }

    @Override
    public void deleteRestaurant(Long restaurantId) {
        try {
            elasticsearchClient.delete(DeleteRequest.of(b -> b
                    .index(restaurantsIndex)
                    .id(String.valueOf(restaurantId))
            ));
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete restaurant from Elasticsearch", e);
        }
    }

    @Override
    public void updateRestaurant(RestaurantSearchDocument document) {
        upsertRestaurant(document);
    }

    @Override
    public RestaurantSearchResponseDTO searchRestaurants(RestaurantSearchRequestDTO request) {
        long startedAt = System.nanoTime();
        try {
            SearchRequest searchRequest = SearchRequest.of(b -> b
                    .index(restaurantsIndex)
                    .query(buildRestaurantsQuery(request))
                    .from(Optional.ofNullable(request.getOffset()).orElse(0))
                    .size(Optional.ofNullable(request.getLimit()).orElse(20))
            );

            SearchResponse<Map> response = elasticsearchClient.search(searchRequest, Map.class);

            List<RestaurantSearchDocument> docs = new ArrayList<>();
            response.hits().hits().forEach(hit -> {
                Map source = hit.source();
                if (source == null) {
                    return;
                }
                docs.add(mapToDocument(source));
            });

            List<Long> ids = docs.stream()
                    .map(RestaurantSearchDocument::getId)
                    .filter(Objects::nonNull)
                    .toList();

            Map<Long, Restaurant> restaurantById = new HashMap<>();
            for (Long id : ids) {
                restaurantRepository.findById(id).ifPresent(r -> restaurantById.put(id, r));
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

                boolean isOpen = restaurantUtils.checkIfRestaurantIsOpen(restaurant.getId());
                if (isOpen) {
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

            RestaurantSearchResponseDTO dto = new RestaurantSearchResponseDTO();
            dto.setQuery(request.getQuery() == null ? "" : request.getQuery());
            dto.setLimit(Optional.ofNullable(request.getLimit()).orElse(20));
            dto.setOffset(Optional.ofNullable(request.getOffset()).orElse(0));
            dto.setEstimatedTotalHits(response.hits().total() == null ? 0L : response.hits().total().value());
            dto.setProcessingTimeMs((int) Duration.ofNanos(System.nanoTime() - startedAt).toMillis());
            dto.setHits(hits);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to search restaurants in Elasticsearch", e);
        }
    }

    private void upsertRestaurant(RestaurantSearchDocument document) {
        try {
            Map<String, Object> source = documentToMap(document);

            IndexRequest<Map<String, Object>> req = IndexRequest.of(b -> b
                    .index(restaurantsIndex)
                    .id(String.valueOf(document.getId()))
                    .document(source)
            );

            elasticsearchClient.index(req);
        } catch (Exception e) {
            throw new RuntimeException("Failed to index restaurant in Elasticsearch", e);
        }
    }

    public void indexMenuItem(MenuItemSearchDocument document) {
        upsertMenuItem(document);
    }

    public void indexMenuItems(List<MenuItemSearchDocument> documents) {
        for (MenuItemSearchDocument document : documents) {
            upsertMenuItem(document);
        }
    }

    public void deleteMenuItem(Long menuItemId) {
        try {
            elasticsearchClient.delete(DeleteRequest.of(b -> b
                    .index(menuItemsIndex)
                    .id(String.valueOf(menuItemId))
            ));
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete menu item from Elasticsearch", e);
        }
    }

    private void upsertMenuItem(MenuItemSearchDocument document) {
        try {
            Map<String, Object> source = menuItemDocumentToMap(document);

            IndexRequest<Map<String, Object>> req = IndexRequest.of(b -> b
                    .index(menuItemsIndex)
                    .id(String.valueOf(document.getMenuItemId()))
                    .document(source)
            );

            elasticsearchClient.index(req);
        } catch (Exception e) {
            throw new RuntimeException("Failed to index menu item in Elasticsearch", e);
        }
    }

    private Query buildRestaurantsQuery(RestaurantSearchRequestDTO request) {
        BoolQuery.Builder bool = new BoolQuery.Builder();

        String q = request.getQuery() == null ? "" : request.getQuery().trim();
        if (!q.isEmpty()) {
            bool.must(m -> m.multiMatch(mm -> mm
                    .query(q)
                    .fields("name", "slug", "categories", "menuItems")
            ));
        } else {
            bool.must(m -> m.matchAll(ma -> ma));
        }

        if (request.getCategoryName() != null && !request.getCategoryName().isBlank()) {
            bool.filter(f -> f.term(t -> t.field("categories").value(request.getCategoryName())));
        }

        if (request.getMinRating() != null) {
            bool.filter(f -> f.range(r -> r.field("ratingAvg").gte(JsonData.of(request.getMinRating()))));
        }

        BigDecimal lat = request.getLat();
        BigDecimal lng = request.getLng();
        if (lat != null && lng != null) {
            int radiusMeters = request.getRadius() != null ? request.getRadius() * 1000 : DEFAULT_RADIUS_METERS;
            bool.filter(f -> f.geoDistance(g -> g
                    .field("geo")
                    .distance(radiusMeters + "m")
                    .location(l -> l.text(lat.doubleValue() + "," + lng.doubleValue()))
            ));
        }

        // request.isOpen is computed at runtime via RestaurantUtils; we do not filter in ES for now.
        return new Query.Builder().bool(bool.build()).build();
    }

    private Map<String, Property> restaurantsMapping() {
        Map<String, Property> props = new HashMap<>();
        props.put("id", Property.of(p -> p.long_(l -> l)));
        props.put("name", Property.of(p -> p.text(t -> t.fields("keyword", k -> k.keyword(kk -> kk)))));
        props.put("slug", Property.of(p -> p.keyword(k -> k)));
        props.put("geo", Property.of(p -> p.geoPoint(g -> g)));
        props.put("ratingAvg", Property.of(p -> p.double_(d -> d)));
        props.put("ratingCount", Property.of(p -> p.integer(i -> i)));
        props.put("prepTimeAvg", Property.of(p -> p.integer(i -> i)));
        props.put("categories", Property.of(p -> p.keyword(k -> k)));
        props.put("menuItems", Property.of(p -> p.text(t -> t)));
        return props;
    }

    private Map<String, Property> menuItemsMapping() {
        Map<String, Property> props = new HashMap<>();
        props.put("menuItemId", Property.of(p -> p.long_(l -> l)));
        props.put("embeddingText", Property.of(p -> p.text(t -> t)));
        props.put("embedding", Property.of(p -> p.denseVector(dv -> dv
                .dims(menuItemEmbeddingDims)
                .index(true)
        )));
        return props;
    }

    private void ensureIndexExists(String indexName, Map<String, Property> mappings) throws Exception {
        boolean exists = elasticsearchClient.indices()
                .exists(ExistsRequest.of(b -> b.index(indexName)))
                .value();

        if (exists) {
            return;
        }

        CreateIndexRequest request = CreateIndexRequest.of(b -> b
                .index(indexName)
                .mappings(mb -> mb.properties(mappings))
        );

        elasticsearchClient.indices().create(request);
    }

    private static Map<String, Object> documentToMap(RestaurantSearchDocument document) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", document.getId());
        map.put("name", document.getName());
        map.put("slug", document.getSlug());
        map.put("ratingAvg", document.getRatingAvg());
        map.put("ratingCount", document.getRatingCount());
        map.put("prepTimeAvg", document.getPrepTimeAvg());
        map.put("categories", document.getCategories());
        map.put("menuItems", document.getMenuItems());

        if (document.getGeo() != null) {
            Map<String, Object> geo = new HashMap<>();
            geo.put("lat", document.getGeo().getLat());
            geo.put("lon", document.getGeo().getLng());
            map.put("geo", geo);
        }

        return map;
    }

    private static Map<String, Object> menuItemDocumentToMap(MenuItemSearchDocument document) {
        Map<String, Object> map = new HashMap<>();
        map.put("menuItemId", document.getMenuItemId());
        map.put("embeddingText", document.getEmbeddingText());
        map.put("embedding", document.getEmbedding());
        return map;
    }

    private static RestaurantSearchDocument mapToDocument(Map source) {
        RestaurantSearchDocument doc = new RestaurantSearchDocument();
        Object id = source.get("id");
        if (id instanceof Number n) {
            doc.setId(n.longValue());
        } else if (id != null) {
            doc.setId(Long.valueOf(String.valueOf(id)));
        }

        doc.setName(source.get("name") == null ? null : String.valueOf(source.get("name")));
        doc.setSlug(source.get("slug") == null ? null : String.valueOf(source.get("slug")));

        Object ratingAvg = source.get("ratingAvg");
        if (ratingAvg instanceof Number n) {
            doc.setRatingAvg(n.doubleValue());
        }
        Object ratingCount = source.get("ratingCount");
        if (ratingCount instanceof Number n) {
            doc.setRatingCount(n.intValue());
        }
        Object prepTimeAvg = source.get("prepTimeAvg");
        if (prepTimeAvg instanceof Number n) {
            doc.setPrepTimeAvg(n.intValue());
        }

        Object categories = source.get("categories");
        if (categories instanceof List<?> list) {
            //noinspection unchecked
            doc.setCategories((List<String>) list);
        }

        Object menuItems = source.get("menuItems");
        if (menuItems instanceof List<?> list) {
            //noinspection unchecked
            doc.setMenuItems((List<String>) list);
        }

        Object geoObj = source.get("geo");
        if (geoObj instanceof Map<?, ?> geoMap) {
            Object lat = geoMap.get("lat");
            Object lon = geoMap.get("lon");
            if (lat instanceof Number latN && lon instanceof Number lonN) {
                doc.setGeo(new RestaurantSearchDocument.GeoPoint(latN.doubleValue(), lonN.doubleValue()));
            }
        }

        return doc;
    }
}

