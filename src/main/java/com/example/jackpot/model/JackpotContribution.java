package com.example.jackpot.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "jackpot_contribution")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JackpotContribution {

    @Id
    private String betId;
    private String userId;
    private String jackpotId;
    private BigDecimal stakeAmount;
    private BigDecimal contributionAmount;
    private BigDecimal currentJackpotAmount;
    private Instant createdAt;
    private boolean evaluated;
}
