package com.example.jackpot.strategy;

import com.example.jackpot.model.ContributionType;
import com.example.jackpot.model.Jackpot;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class FixedContributionStrategy implements ContributionStrategy {
    @Override
    public ContributionType type() {
        return ContributionType.FIXED;
    }

    @Override
    public BigDecimal calculate(Jackpot jackpot, BigDecimal stake) {
        return stake.multiply(jackpot.getContributionPercentage())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}
