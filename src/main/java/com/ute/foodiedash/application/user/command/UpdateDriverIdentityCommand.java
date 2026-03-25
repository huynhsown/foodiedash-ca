package com.ute.foodiedash.application.user.command;

public record UpdateDriverIdentityCommand(
        String idCardNumber,
        String idCardFrontUrl,
        String idCardBackUrl
) {
}
