package com.example.jackpot.controller;

import com.example.jackpot.dto.BetRequest;
import com.example.jackpot.dto.EvaluationResponse;
import com.example.jackpot.service.BetProcessingService;
import com.example.jackpot.service.BetPublisher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bets")
@Tag(name = "Bets")
public class BetController {

    private final BetPublisher publisher;
    private final BetProcessingService processing;

    public BetController(BetPublisher publisher, BetProcessingService processing) {
        this.publisher = publisher;
        this.processing = processing;
    }

    @Operation(summary = "Publish a bet to the jackpot-bets Kafka topic",
            description = "Processing (contribution + reward evaluation) happens asynchronously in the consumer.")
    @ApiResponse(responseCode = "202", description = "Bet accepted and published")
    @ApiResponse(responseCode = "400", description = "Validation error")
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public BetRequest publish(@Valid @RequestBody BetRequest bet) {
        publisher.publish(bet);
        return bet;
    }

    @Operation(summary = "Evaluate whether a contributing bet wins the jackpot",
            description = "A bet is rolled only once; repeat calls return the stored outcome.")
    @ApiResponse(responseCode = "200", description = "Evaluation result")
    @ApiResponse(responseCode = "404", description = "Bet has not contributed to any jackpot")
    @PostMapping("/{betId}/evaluate")
    public EvaluationResponse evaluate(@Parameter(example = "bet-1") @PathVariable String betId) {
        return processing.evaluateBet(betId);
    }
}
