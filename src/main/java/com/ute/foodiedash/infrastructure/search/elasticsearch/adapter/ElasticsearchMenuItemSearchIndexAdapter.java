package com.ute.foodiedash.infrastructure.search.elasticsearch.adapter;

import com.ute.foodiedash.application.menu.port.MenuItemSearchIndexPort;
import com.ute.foodiedash.domain.menu.model.MenuItem;
import com.ute.foodiedash.domain.menu.repository.MenuItemRepository;
import com.ute.foodiedash.infrastructure.search.elasticsearch.impl.ElasticsearchServiceImpl;
import com.ute.foodiedash.infrastructure.search.meilisearch.MenuItemSearchDocumentService;
import com.ute.foodiedash.infrastructure.search.meilisearch.docs.MenuItemSearchDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ElasticsearchMenuItemSearchIndexAdapter implements MenuItemSearchIndexPort {

    private final MenuItemRepository menuItemRepository;
    private final MenuItemSearchDocumentService menuItemSearchDocumentService;
    private final ElasticsearchServiceImpl elasticsearchService;

    @Override
    public void indexMenuItem(Long menuItemId) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId).orElse(null);
        if (menuItem == null) {
            return;
        }

        MenuItemSearchDocument document = menuItemSearchDocumentService.toDocument(menuItem);
        elasticsearchService.indexMenuItem(document);
    }

    @Override
    public void deleteMenuItem(Long menuItemId) {
        elasticsearchService.deleteMenuItem(menuItemId);
    }
}
