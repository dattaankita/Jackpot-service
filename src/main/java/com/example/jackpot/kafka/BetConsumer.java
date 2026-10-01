package com.example.jackpot.kafka;

import com.example.jackpot.dto.BetRequest;
import com.example.jackpot.service.BetProcessingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BetConsumer {
    private static final Logger log = LoggerFactory.getLogger(BetConsumer.class);

    private final BetProcessingService processing;
    private final ObjectMapper mapper;

    public BetConsumer(BetProcessingService processing, ObjectMapper mapper) {
        this.processing = processing;
        this.mapper = mapper;
    }

    @KafkaListener(topics = "${jackpot.kafka.topic}", autoStartup = "${jackpot.kafka.enabled}")
    public void onMessage(String payload) throws Exception {
        BetRequest bet = mapper.readValue(payload, BetRequest.class);
        log.info("Consumed bet {}", bet.betId());
        processing.process(bet);
    }
}
