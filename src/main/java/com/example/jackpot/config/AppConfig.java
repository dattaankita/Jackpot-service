package com.example.jackpot.config;

import com.example.jackpot.model.*;
import com.example.jackpot.repository.JackpotRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.random.RandomGenerator;

@Configuration
public class AppConfig {

    @Bean
    public RandomGenerator randomGenerator() {
        return new java.security.SecureRandom();
    }

    @Bean
    CommandLineRunner seedJackpots(JackpotRepository repo) {
        return args -> {
            repo.save(Jackpot.builder().id("JP-FIXED").name("Fixed jackpot")
                    .initialPool(bd("1000")).currentPool(bd("1000"))
                    .contributionType(ContributionType.FIXED).contributionPercentage(bd("5"))
                    .rewardType(RewardType.FIXED).rewardChancePercentage(bd("2"))
                    .build());
            repo.save(Jackpot.builder().id("JP-VARIABLE").name("Variable jackpot")
                    .initialPool(bd("1000")).currentPool(bd("1000"))
                    .contributionType(ContributionType.VARIABLE).contributionPercentage(bd("10"))
                    .contributionMinPercentage(bd("2")).contributionDecayPer1000(bd("1"))
                    .rewardType(RewardType.VARIABLE).rewardChancePercentage(bd("1"))
                    .rewardPoolLimit(bd("5000"))
                    .build());
        };
    }

    private static BigDecimal bd(String v) {
        return new BigDecimal(v); }
}
