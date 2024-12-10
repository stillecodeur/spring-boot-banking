package com.anirudh.bankingapp.dto;

public record TransferFundDto(
        Long fromAccountId,
        Long toAccountId,
        double amount
) {
}
