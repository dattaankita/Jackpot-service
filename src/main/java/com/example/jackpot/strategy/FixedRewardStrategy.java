package com.example.jackpot.strategy;

import com.example.jackpot.model.Jackpot;
import com.example.jackpot.model.RewardType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FixedRewardStrategy implements RewardStrategy {

    @Override
    public RewardType type() {
        return RewardType.FIXED;
    }

    @Override
    public BigDecimal winChancePercent(Jackpot jackpot) {
        return jackpot.getRewardChancePercentage();
    }
}
