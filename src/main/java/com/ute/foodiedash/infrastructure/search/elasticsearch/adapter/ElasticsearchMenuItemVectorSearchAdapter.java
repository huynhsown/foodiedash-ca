package com.ute.foodiedash.infrastructure.search.elasticsearch.adapter;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.ute.foodiedash.application.menu.port.MenuItemVectorSearchPort;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "search.engine", havingValue = "elasticsearch")
public class ElasticsearchMenuItemVectorSearchAdapter implements MenuItemVectorSearchPort {

    private final ElasticsearchClient elasticsearchClient;

    @Value("${elasticsearch.index.menu-items:menu-items}")
    private String menuItemsIndex;

    @Override
    public List<VectorMatch> findMatches(List<Float> queryVector, int candidateLimit, int topK) {
        if (queryVector == null || queryVector.isEmpty()) {
            return List.of();
        }

        try {
            int numCandidates = Math.max(candidateLimit, 1);
            int size = Math.max(topK, 1);
            SearchRequest searchRequest = SearchRequest.of(b -> b
                    .index(menuItemsIndex)
                    .knn(k -> k
                            .field("embedding")
                            .queryVector(queryVector)
                            .k((long) size)
                            .numCandidates((long) numCandidates)
                    )
                    .size(size)
            );
            SearchResponse<Map> response = elasticsearchClient.search(searchRequest, Map.class);

            var hits = response.hits().hits();
            if (hits.isEmpty()) {
                return List.of();
            }

            return hits.stream()
                    .map(hit -> {
                        Map source = hit.source();
                        if (source == null) {
                            return null;
                        }
                        Long menuItemId = toLong(source.get("menuItemId"));
                        if (menuItemId == null) {
                            return null;
                        }
                        double score = hit.score() == null ? 0.0 : hit.score().doubleValue();
                        return new VectorMatch(menuItemId, score);
                    })
                    .filter(match -> match != null)
                    .toList();
        } catch (Exception e) {
            log.warn("kNN search failed on index {}. Falling back to local cosine search. Error: {}",
                    menuItemsIndex, e.getMessage());
            return findMatchesFallback(queryVector, candidateLimit, topK);
        }
    }

    private List<VectorMatch> findMatchesFallback(List<Float> queryVector, int candidateLimit, int topK) {
        try {
            SearchRequest searchRequest = SearchRequest.of(b -> b
                    .index(menuItemsIndex)
                    .query(q -> q.matchAll(m -> m))
                    .size(Math.max(candidateLimit, 1))
            );
            SearchResponse<Map> response = elasticsearchClient.search(searchRequest, Map.class);

            List<VectorMatch> matches = new ArrayList<>();
            for (var hit : response.hits().hits()) {
                Map source = hit.source();
                if (source == null) {
                    continue;
                }

                Long menuItemId = toLong(source.get("menuItemId"));
                List<Float> candidateVector = toFloatList(source.get("embedding"));
                if (menuItemId == null || candidateVector.isEmpty()) {
                    continue;
                }

                double score = cosineSimilarity(queryVector, candidateVector);
                matches.add(new VectorMatch(menuItemId, score));
            }

            return matches.stream()
                    .sorted(Comparator.comparingDouble(VectorMatch::score).reversed())
                    .limit(Math.max(topK, 1))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new RuntimeException("Failed to run menu item vector search in Elasticsearch", ex);
        }
    }

    private static Long toLong(Object value) {
        if (value instanceof Number n) {
            return n.longValue();
        }
        if (value != null) {
            return Long.valueOf(String.valueOf(value));
        }
        return null;
    }

    private static List<Float> toFloatList(Object value) {
        if (!(value instanceof List<?> list)) {
            return List.of();
        }
        List<Float> result = new ArrayList<>(list.size());
        for (Object item : list) {
            if (item instanceof Number n) {
                result.add(n.floatValue());
            }
        }
        return result;
    }

    private static double cosineSimilarity(List<Float> a, List<Float> b) {
        int size = Math.min(a.size(), b.size());
        if (size == 0) {
            return -1.0;
        }

        double dot = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < size; i++) {
            float av = a.get(i);
            float bv = b.get(i);
            dot += av * bv;
            normA += av * av;
            normB += bv * bv;
        }

        if (normA == 0.0 || normB == 0.0) {
            return -1.0;
        }

        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
