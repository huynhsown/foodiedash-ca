package com.ute.foodiedash.infrastructure.search.elasticsearch.config;

import com.ute.foodiedash.infrastructure.search.meilisearch.MeilisearchService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "search.engine", havingValue = "elasticsearch")
@ConditionalOnProperty(name = "elasticsearch.auto-initialize", havingValue = "true", matchIfMissing = true)
public class ElasticsearchInitializer {

    private final MeilisearchService searchService;

    @PostConstruct
    public void initialize() {
        try {
            log.info("Initializing Elasticsearch index...");
            searchService.initializeIndex();
            log.info("Elasticsearch index initialized successfully");
        } catch (Exception e) {
            log.error("Failed to initialize Elasticsearch index", e);
        }
    }
}

