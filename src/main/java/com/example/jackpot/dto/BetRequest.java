package com.example.jackpot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record BetRequest(
        @Schema(example = "bet-1") @NotBlank String betId,
        @Schema(example = "user-1") @NotBlank String userId,
        @Schema(description = "Seeded jackpots: JP-FIXED, JP-VARIABLE", example = "JP-FIXED")
        @NotBlank String jackpotId,
        @Schema(example = "100.00") @NotNull @DecimalMin("0.01") BigDecimal betAmount) {
}
