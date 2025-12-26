package com.ute.foodiedash.application.user.command;

public record ChangeCustomerPasswordCommand(
        String currentPassword,
        String newPassword
) {
}

