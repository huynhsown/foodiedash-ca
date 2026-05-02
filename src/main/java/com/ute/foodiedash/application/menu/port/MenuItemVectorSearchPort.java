package com.ute.foodiedash.application.menu.port;

import java.util.List;

public interface MenuItemVectorSearchPort {
    List<VectorMatch> findMatches(List<Float> queryVector, int candidateLimit, int topK);

    record VectorMatch(Long menuItemId, double score) {}
}
