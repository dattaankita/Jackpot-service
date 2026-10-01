package com.example.jackpot.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "jackpot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Jackpot {

    @Id
    private String id;
    private String name;

    @Column(precision = 19, scale = 2)
    private BigDecimal initialPool;
    @Column(precision = 19, scale = 2)
    private BigDecimal currentPool;

    // ---- contribution configuration ----
    @Enumerated(EnumType.STRING)
    private ContributionType contributionType;

    private BigDecimal contributionPercentage;

    private BigDecimal contributionMinPercentage;

    private BigDecimal contributionDecayPer1000;

    // ---- reward configuration ----
    @Enumerated(EnumType.STRING)
    private RewardType rewardType;

    private BigDecimal rewardChancePercentage;

    private BigDecimal rewardPoolLimit;
}
