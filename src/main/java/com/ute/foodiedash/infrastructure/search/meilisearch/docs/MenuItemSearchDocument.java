package com.ute.foodiedash.infrastructure.search.meilisearch.docs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemSearchDocument {
    private Long menuItemId;

    private String embeddingText;

    private List<Float> embedding;
}
