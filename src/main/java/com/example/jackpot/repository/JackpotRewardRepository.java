package com.example.jackpot.repository;

import com.example.jackpot.model.JackpotReward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JackpotRewardRepository extends JpaRepository<JackpotReward, String> {
}
