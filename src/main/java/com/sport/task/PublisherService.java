package com.sport.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
class PublisherService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void publish(String key, ScoreUpdate payload) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("sports-scores", objectMapper.writeValueAsString(payload));
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Published: key={}, partition={}, offset={}",
                        key,
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                log.error("Publish failed: key={}, error={}", key, ex.toString());
                // Optionally retry or send to DLQ
            }
        });
    }
}
