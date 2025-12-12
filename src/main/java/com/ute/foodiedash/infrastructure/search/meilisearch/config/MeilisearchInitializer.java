package com.ute.foodiedash.infrastructure.search.meilisearch.config;

import com.ute.foodiedash.infrastructure.search.meilisearch.MeilisearchService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "search.engine", havingValue = "meilisearch")
@ConditionalOnProperty(name = "meilisearch.auto-initialize", havingValue = "true", matchIfMissing = true)
public class MeilisearchInitializer {

    private final MeilisearchService meilisearchService;

    @PostConstruct
    public void initialize() {
        try {
            log.info("Initializing Meilisearch index...");
            meilisearchService.initializeIndex();
            log.info("Meilisearch index initialized successfully");
        } catch (Exception e) {
            log.error("Failed to initialize Meilisearch index", e);
        }
    }
}
