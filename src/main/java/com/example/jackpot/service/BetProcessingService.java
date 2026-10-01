package com.example.jackpot.service;

import com.example.jackpot.dto.BetRequest;
import com.example.jackpot.dto.EvaluationResponse;
import com.example.jackpot.model.*;
import com.example.jackpot.repository.*;
import com.example.jackpot.strategy.ContributionStrategy;
import com.example.jackpot.strategy.RewardStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;

@Service
public class BetProcessingService {
    private static final Logger log = LoggerFactory.getLogger(BetProcessingService.class);
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    private final JackpotRepository jackpots;
    private final JackpotContributionRepository contributions;
    private final JackpotRewardRepository rewards;
    private final Map<ContributionType, ContributionStrategy> contributionStrategies;
    private final Map<RewardType, RewardStrategy> rewardStrategies;
    private final RandomGenerator random;

    public BetProcessingService(JackpotRepository jackpots, JackpotContributionRepository contributions,
                                JackpotRewardRepository rewards, List<ContributionStrategy> contributionStrategies,
                                List<RewardStrategy> rewardStrategies, RandomGenerator random) {
        this.jackpots = jackpots;
        this.contributions = contributions;
        this.rewards = rewards;
        this.contributionStrategies = contributionStrategies.stream()
                .collect(Collectors.toMap(ContributionStrategy::type, Function.identity()));
        this.rewardStrategies = rewardStrategies.stream()
                .collect(Collectors.toMap(RewardStrategy::type, Function.identity()));
        this.random = random;
    }

    @Transactional
    public void process(BetRequest bet) {
        if (contributions.existsById(bet.betId())) {
            log.info("Bet {} already processed, ignoring duplicate", bet.betId());
            return;
        }
        Jackpot jackpot = jackpots.findByIdForUpdate(bet.jackpotId()).orElse(null);
        if (jackpot == null) {
            log.info("No jackpot {} for bet {}, ignoring", bet.jackpotId(), bet.betId());
            return;
        }

        BigDecimal amount = contributionStrategies.get(jackpot.getContributionType()).calculate(jackpot, bet.betAmount());
        jackpot.setCurrentPool(jackpot.getCurrentPool().add(amount));

        JackpotContribution contribution = contributions.save(JackpotContribution.builder()
                .betId(bet.betId()).userId(bet.userId()).jackpotId(jackpot.getId())
                .stakeAmount(bet.betAmount()).contributionAmount(amount)
                .currentJackpotAmount(jackpot.getCurrentPool()).createdAt(Instant.now()).build());
        log.info("Bet {} contributed {} to {} (pool now {})", bet.betId(), amount, jackpot.getId(), jackpot.getCurrentPool());

        evaluate(contribution, jackpot);
    }

    @Transactional
    public EvaluationResponse evaluateBet(String betId) {
        JackpotContribution contribution = contributions.findById(betId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Bet " + betId + " has not contributed to any jackpot"));

        Jackpot jackpot = jackpots.findByIdForUpdate(contribution.getJackpotId()).orElseThrow();
        contribution = contributions.findById(betId).orElseThrow();
        if (!contribution.isEvaluated()) {
            evaluate(contribution, jackpot);
        }
        return rewards.findById(betId)
                .map(r -> new EvaluationResponse(betId, r.getJackpotId(), true, r.getJackpotRewardAmount()))
                .orElseGet(() -> new EvaluationResponse(betId, jackpot.getId(), false, BigDecimal.ZERO));
    }

    private void evaluate(JackpotContribution contribution, Jackpot jackpot) {
        BigDecimal chance = rewardStrategies.get(jackpot.getRewardType()).winChancePercent(jackpot);
        boolean won = chance.compareTo(HUNDRED) >= 0
                || BigDecimal.valueOf(random.nextDouble() * 100).compareTo(chance) < 0;

        if (won) {
            BigDecimal rewardAmount = jackpot.getCurrentPool();
            rewards.save(JackpotReward.builder().betId(contribution.getBetId()).userId(contribution.getUserId())
                    .jackpotId(jackpot.getId()).jackpotRewardAmount(rewardAmount).createdAt(Instant.now()).build());
            jackpot.setCurrentPool(jackpot.getInitialPool());
            log.info("Bet {} WON {} from {} (chance {}%); pool reset to {}",
                    contribution.getBetId(), rewardAmount, jackpot.getId(), chance, jackpot.getInitialPool());
        } else {
            log.info("Bet {} did not win {} (chance {}%)", contribution.getBetId(), jackpot.getId(), chance);
        }
        contribution.setEvaluated(true);
    }
}
