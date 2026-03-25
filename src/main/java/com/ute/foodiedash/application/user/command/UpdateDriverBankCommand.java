package com.ute.foodiedash.application.user.command;

public record UpdateDriverBankCommand(
        String bankName,
        String bankAccount,
        String bankHolderName
) {
}
