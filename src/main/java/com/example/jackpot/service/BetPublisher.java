package com.example.jackpot.service;

import com.example.jackpot.dto.BetRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class BetPublisher {
    private static final Logger log = LoggerFactory.getLogger(BetPublisher.class);

    private final KafkaTemplate<String, String> kafka;
    private final ObjectMapper mapper;
    private final BetProcessingService processing;
    private final boolean kafkaEnabled;
    private final String topic;

    public BetPublisher(KafkaTemplate<String, String> kafka, ObjectMapper mapper,
                        BetProcessingService processing,
                        @Value("${jackpot.kafka.enabled}") boolean kafkaEnabled,
                        @Value("${jackpot.kafka.topic}") String topic) {
        this.kafka = kafka;
        this.mapper = mapper;
        this.processing = processing;
        this.kafkaEnabled = kafkaEnabled;
        this.topic = topic;
    }

    public void publish(BetRequest bet) {
        try {
            String payload = mapper.writeValueAsString(bet);
            if (kafkaEnabled) {
                kafka.send(topic, bet.jackpotId(), payload);
            } else {
                log.info("topic={} payload={}", topic, payload);
                processing.process(bet);
            }
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}
