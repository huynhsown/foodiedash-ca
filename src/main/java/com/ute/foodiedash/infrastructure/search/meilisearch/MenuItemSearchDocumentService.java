package com.ute.foodiedash.infrastructure.search.meilisearch;

import com.ute.foodiedash.domain.menu.model.MenuItem;
import com.ute.foodiedash.infrastructure.search.meilisearch.docs.MenuItemSearchDocument;

public interface MenuItemSearchDocumentService {
    MenuItemSearchDocument toDocument(MenuItem menuItem);
}
