package com.ute.foodiedash.application.menu.usecase;

import com.ute.foodiedash.application.menu.command.CreateMenuItemCommand;
import com.ute.foodiedash.application.menu.port.ImageUploadPort;
import com.ute.foodiedash.application.menu.port.MenuItemSearchIndexPort;
import com.ute.foodiedash.domain.menu.enums.MenuItemStatus;
import com.ute.foodiedash.domain.menu.enums.MenuStatus;
import com.ute.foodiedash.domain.menu.model.Menu;
import com.ute.foodiedash.domain.menu.model.MenuItem;
import com.ute.foodiedash.domain.menu.repository.MenuItemRepository;
import com.ute.foodiedash.domain.menu.repository.MenuRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuItemEventPublishingUseCaseTest {

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuItemRepository menuItemRepository;
    @Mock
    private ImageUploadPort imageUploadPort;
    @Mock
    private MenuItemSearchIndexPort menuItemSearchIndexPort;

    @InjectMocks
    private CreateMenuItemUseCase createMenuItemUseCase;

    @InjectMocks
    private SoftDeleteMenuItemUseCase softDeleteMenuItemUseCase;

    @InjectMocks
    private RestoreMenuItemUseCase restoreMenuItemUseCase;

    @Test
    void createMenuItem_shouldIndexCreatedMenuItem() {
        menuItemSearchIndexPort.indexMenuItem(9L);
    }

    @Test
    void softDeleteMenuItem_shouldDeleteMenuItemFromIndex() {
        MenuItem existing = MenuItem.reconstruct(
                50L,
                10L,
                20L,
                "Pho",
                "Pho bo",
                BigDecimal.valueOf(50000),
                null,
                MenuItemStatus.ACTIVE,
                List.of(),
                Instant.now(),
                Instant.now(),
                "test",
                "test",
                null,
                0L
        );
        when(menuItemRepository.findById(50L)).thenReturn(Optional.of(existing));

        softDeleteMenuItemUseCase.execute(50L);

        verify(menuItemSearchIndexPort).deleteMenuItem(50L);
    }

    @Test
    void restoreMenuItem_shouldReindexMenuItem() {
        MenuItem existing = MenuItem.reconstruct(
                60L,
                10L,
                20L,
                "Pho",
                "Pho bo",
                BigDecimal.valueOf(50000),
                null,
                MenuItemStatus.ACTIVE,
                List.of(),
                Instant.now(),
                Instant.now(),
                "test",
                "test",
                null,
                0L
        );
        when(menuItemRepository.findById(60L)).thenReturn(Optional.of(existing));

        restoreMenuItemUseCase.execute(60L);

        verify(menuItemSearchIndexPort).indexMenuItem(60L);
    }
}
