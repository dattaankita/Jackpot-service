package com.example.jackpot.strategy;

import com.example.jackpot.model.ContributionType;
import com.example.jackpot.model.Jackpot;

import java.math.BigDecimal;

public interface ContributionStrategy {

    ContributionType type();

    BigDecimal calculate(Jackpot jackpot, BigDecimal stake);
}
