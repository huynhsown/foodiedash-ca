package com.ute.foodiedash.application.menu.usecase;

import com.ute.foodiedash.application.ai.port.EmbeddingPort;
import com.ute.foodiedash.application.menu.port.MenuItemVectorSearchPort;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "search.engine", havingValue = "elasticsearch")
public class SearchMenuItemByMessageUseCase {

    private static final int DEFAULT_CANDIDATE_LIMIT = 500;
    private static final int DEFAULT_TOP_K = 5;

    private final EmbeddingPort embeddingPort;
    private final MenuItemVectorSearchPort menuItemVectorSearchPort;

    public List<Result> execute(String message) {
        var embedding = embeddingPort.embed(message);
        var matches = menuItemVectorSearchPort.findMatches(embedding, DEFAULT_CANDIDATE_LIMIT, DEFAULT_TOP_K);
        return matches.stream()
                .map(match -> new Result(match.menuItemId(), match.score()))
                .toList();
    }

    public record Result(Long menuItemId, Double score) {}
}
