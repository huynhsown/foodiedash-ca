package com.ute.foodiedash.application.menu.command;

public record CreateMenuItemOptionCommand(
    Long menuItemId,
    String name,
    Boolean required,
    Integer minValue,
    Integer maxValue
) {}
