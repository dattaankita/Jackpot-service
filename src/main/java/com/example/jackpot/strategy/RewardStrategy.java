package com.example.jackpot.strategy;

import com.example.jackpot.model.Jackpot;
import com.example.jackpot.model.RewardType;

import java.math.BigDecimal;


public interface RewardStrategy {

    RewardType type();

    BigDecimal winChancePercent(Jackpot jackpot);
}
