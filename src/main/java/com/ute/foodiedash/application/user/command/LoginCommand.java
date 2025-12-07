package com.ute.foodiedash.application.user.command;

public record LoginCommand(
        String email,
        String password
) {}
