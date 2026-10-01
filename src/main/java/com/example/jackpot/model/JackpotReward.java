package com.example.jackpot.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "jackpot_reward")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JackpotReward {

    @Id
    private String betId;
    private String userId;
    private String jackpotId;
    private BigDecimal jackpotRewardAmount;
    private Instant createdAt;
}
