package com.example.jackpot.repository;

import com.example.jackpot.model.Jackpot;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JackpotRepository extends JpaRepository<Jackpot, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select j from Jackpot j where j.id = :id")
    Optional<Jackpot> findByIdForUpdate(@Param("id") String id);
}
