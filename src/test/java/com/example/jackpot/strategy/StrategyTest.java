package com.example.jackpot.strategy;

import com.example.jackpot.model.Jackpot;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StrategyTest {

    private static BigDecimal bd(String v) { return new BigDecimal(v); }

    private Jackpot jackpot(String pool) {
        return Jackpot.builder().initialPool(bd("1000")).currentPool(bd(pool))
                .contributionPercentage(bd("10")).contributionMinPercentage(bd("2"))
                .contributionDecayPer1000(bd("1"))
                .rewardChancePercentage(bd("1"))
                .rewardPoolLimit(bd("5000"))
                .build();
    }

    @Test
    void fixedContributionIsPercentageOfStake() {
        assertEquals(bd("5.00"), new FixedContributionStrategy()
                .calculate(jackpot("1000"), bd("100")));
    }

    @Test
    void variableContributionDecaysAsPoolGrows() {
        var s = new VariableContributionStrategy();
        assertEquals(bd("10.00"), s.calculate(jackpot("1000"), bd("100")));   // 10%
        assertEquals(bd("8.00"), s.calculate(jackpot("3000"), bd("100")));    // 10 - 2 = 8%
        assertEquals(bd("2.00"), s.calculate(jackpot("50000"), bd("100")));   // floored at 2%
    }

    @Test
    void variableChanceGrowsAndCapsAtHundred() {
        var s = new VariableRewardStrategy();
        assertEquals(0, bd("1").compareTo(s.winChancePercent(jackpot("1000"))));
        assertEquals(0, bd("50.5").compareTo(s.winChancePercent(jackpot("3000"))));
        assertEquals(0, bd("100").compareTo(s.winChancePercent(jackpot("5000"))));
    }

    @Test
    void fixedChanceIsConstant() {
        assertEquals(bd("1"), new FixedRewardStrategy().winChancePercent(jackpot("9999")));
    }
}
