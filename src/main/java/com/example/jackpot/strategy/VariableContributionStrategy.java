package com.example.jackpot.strategy;

import com.example.jackpot.model.ContributionType;
import com.example.jackpot.model.Jackpot;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Component
public class VariableContributionStrategy implements ContributionStrategy {
    @Override
    public ContributionType type() {
        return ContributionType.VARIABLE;
    }

    @Override
    public BigDecimal calculate(Jackpot j, BigDecimal stake) {
        BigDecimal growth = j.getCurrentPool().subtract(j.getInitialPool()).max(BigDecimal.ZERO);

        BigDecimal reduction = j.getContributionDecayPer1000()
                .multiply(growth).divide(BigDecimal.valueOf(1000), 6, RoundingMode.HALF_UP);

        BigDecimal pct = j.getContributionPercentage().subtract(reduction).max(j.getContributionMinPercentage());

        return stake.multiply(pct).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}
