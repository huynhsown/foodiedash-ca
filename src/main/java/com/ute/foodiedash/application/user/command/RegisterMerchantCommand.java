package com.ute.foodiedash.application.user.command;

public record RegisterMerchantCommand(
        String email,
        String phone,
        String password,
        String fullName,
        String businessName,
        String contactEmail,
        String contactPhone
) {}
