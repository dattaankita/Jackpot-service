package com.example.jackpot.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record EvaluationResponse(
        @Schema(example = "bet-1") String betId,
        @Schema(example = "JP-FIXED") String jackpotId,
        @Schema(example = "false") boolean won,
        @Schema(description = "0 when the bet did not win", example = "0") BigDecimal rewardAmount) {
}
