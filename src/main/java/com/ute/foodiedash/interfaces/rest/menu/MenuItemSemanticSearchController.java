package com.ute.foodiedash.interfaces.rest.menu;

import com.ute.foodiedash.application.menu.usecase.SearchMenuItemByMessageUseCase;
import com.ute.foodiedash.interfaces.rest.menu.dto.MenuItemSemanticSearchRequestDTO;
import com.ute.foodiedash.interfaces.rest.menu.dto.MenuItemSemanticSearchResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/menu-items")
@RequiredArgsConstructor
@ConditionalOnProperty(name = "search.engine", havingValue = "elasticsearch")
public class MenuItemSemanticSearchController {

    private final SearchMenuItemByMessageUseCase searchMenuItemByMessageUseCase;

    @PostMapping("/semantic-search")
    public ResponseEntity<MenuItemSemanticSearchResponseDTO> semanticSearch(
            @Valid @RequestBody MenuItemSemanticSearchRequestDTO request
    ) {
        var results = searchMenuItemByMessageUseCase.execute(request.message());
        var matches = results.stream()
                .map(result -> new MenuItemSemanticSearchResponseDTO.ItemMatchDTO(
                        result.menuItemId(),
                        result.score()
                ))
                .toList();
        return ResponseEntity.ok(new MenuItemSemanticSearchResponseDTO(matches));
    }
}
