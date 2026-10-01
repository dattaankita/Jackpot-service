package com.example.jackpot.strategy;

import com.example.jackpot.model.Jackpot;
import com.example.jackpot.model.RewardType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class VariableRewardStrategy implements RewardStrategy {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    @Override
    public RewardType type() {
        return RewardType.VARIABLE;
    }

    @Override
    public BigDecimal winChancePercent(Jackpot j) {
        if (j.getCurrentPool().compareTo(j.getRewardPoolLimit()) >= 0)
            return HUNDRED;

        BigDecimal range = j.getRewardPoolLimit().subtract(j.getInitialPool());
        if (range.signum() <= 0)
            return HUNDRED;

        BigDecimal growth = j.getCurrentPool().subtract(j.getInitialPool()).max(BigDecimal.ZERO);
        BigDecimal start = j.getRewardChancePercentage();
        BigDecimal extra = HUNDRED.subtract(start).multiply(growth).divide(range, 6, RoundingMode.HALF_UP);

        return start.add(extra).min(HUNDRED);
    }
}
