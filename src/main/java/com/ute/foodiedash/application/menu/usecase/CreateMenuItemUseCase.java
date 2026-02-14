package com.ute.foodiedash.application.menu.usecase;

import com.ute.foodiedash.application.menu.command.CreateMenuItemCommand;
import com.ute.foodiedash.application.menu.port.ImageUploadPort;
import com.ute.foodiedash.application.menu.port.MenuItemSearchIndexPort;
import com.ute.foodiedash.application.menu.query.MenuItemQueryResult;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.menu.model.Menu;
import com.ute.foodiedash.domain.menu.model.MenuItem;
import com.ute.foodiedash.domain.menu.repository.MenuItemRepository;
import com.ute.foodiedash.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CreateMenuItemUseCase {
    private final MenuRepository menuRepository;
    private final MenuItemRepository menuItemRepository;
    private final ImageUploadPort imageUploadPort;
    private final MenuItemSearchIndexPort menuItemSearchIndexPort;

    @Transactional
    public MenuItemQueryResult execute(CreateMenuItemCommand command, MultipartFile image) {
        Menu menu = menuRepository.findById(command.menuId())
            .orElseThrow(() -> new NotFoundException("Menu not found with id " + command.menuId()));

        boolean alreadyExists = menuItemRepository.findByMenuId(command.menuId(), false).stream()
            .anyMatch(i -> command.name().equals(i.getName()));
        if (alreadyExists) {
            throw new BadRequestException("Menu item already exists in menu");
        }

        MenuItem menuItem = MenuItem.create(
            menu.getId(),
            menu.getRestaurantId(),
            command.name(),
            command.description(),
            command.price()
        );

        if (image != null && !image.isEmpty()) {
            try {
                Map<String, Object> uploadResult = imageUploadPort.uploadImage(image, "menu-items");
                String imageUrl = uploadResult.get("secure_url").toString();
                menuItem.assignImage(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload menu item image", e);
            }
        } else if (command.imageUrl() != null) {
            menuItem.assignImage(command.imageUrl());
        }

        MenuItem saved = menuItemRepository.save(menuItem);
        menuItemSearchIndexPort.indexMenuItem(saved.getId());

        return new MenuItemQueryResult(
            saved.getId(),
            saved.getMenuId(),
            saved.getRestaurantId(),
            saved.getName(),
            saved.getDescription(),
            saved.getPrice(),
            saved.getImageUrl(),
            saved.getStatus(),
            null
        );
    }
}
