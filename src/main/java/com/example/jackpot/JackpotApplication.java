package com.example.jackpot;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Jackpot Service API", version = "1.0",
        description = "Publish bets to Kafka, contribute to jackpot pools, evaluate jackpot rewards"))
public class JackpotApplication {
    public static void main(String[] args) {
        SpringApplication.run(JackpotApplication.class, args);
    }
}
