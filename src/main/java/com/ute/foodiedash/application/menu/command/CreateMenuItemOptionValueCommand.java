package com.ute.foodiedash.application.menu.command;

import java.math.BigDecimal;

public record CreateMenuItemOptionValueCommand(
    Long optionId,
    String name,
    BigDecimal extraPrice
) {}
