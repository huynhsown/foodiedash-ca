package com.ute.foodiedash.interfaces.rest.menu;

import com.ute.foodiedash.application.menu.usecase.TestMenuItemSearchIndexUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/internal/menu-items")
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.internal-test-apis-enabled", havingValue = "true")
public class MenuItemSearchIndexTestController {

    private final TestMenuItemSearchIndexUseCase testMenuItemSearchIndexUseCase;

    @PostMapping("/{menuItemId}/search-index")
    public ResponseEntity<Map<String, Object>> indexMenuItem(@PathVariable Long menuItemId) {
        testMenuItemSearchIndexUseCase.index(menuItemId);
        return ResponseEntity.ok(Map.of(
                "menuItemId", menuItemId,
                "operation", "INDEX",
                "message", "Menu item indexing triggered."
        ));
    }

    @PostMapping("/{menuItemId}/search-index/delete")
    public ResponseEntity<Map<String, Object>> deleteMenuItemFromIndex(@PathVariable Long menuItemId) {
        testMenuItemSearchIndexUseCase.delete(menuItemId);
        return ResponseEntity.ok(Map.of(
                "menuItemId", menuItemId,
                "operation", "DELETE",
                "message", "Menu item index deletion triggered."
        ));
    }
}
