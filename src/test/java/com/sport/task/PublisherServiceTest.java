package com.sport.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PublisherServiceTest {

    private KafkaTemplate<String, String> kafkaTemplate;
    private PublisherService publisherService;

    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        ObjectMapper objectMapper = new ObjectMapper();
        publisherService = new PublisherService(kafkaTemplate, objectMapper);
    }

    @Test
    void shouldPublishMessageSuccessfully() throws Exception {
        // given
        String topic = "sports-scores";
        String key = "123";
        ScoreUpdate payload = new ScoreUpdate("123", "0:0");
        // symulujemy sukces
        RecordMetadata metadata = new RecordMetadata(null, 0, 42, System.currentTimeMillis(), 0L, 0, 0);
        SendResult<String, String> sendResult = new SendResult<>(null, metadata);
        CompletableFuture<SendResult<String, String>> future = CompletableFuture.completedFuture(sendResult);

        when(kafkaTemplate.send(anyString(), anyString())).thenReturn(future);

        // when
        publisherService.publish(key, payload);

        verify(kafkaTemplate, times(1))
                .send(topic,"{\"eventId\":\"123\",\"currentScore\":\"0:0\"}");
    }

    @Test
    void shouldHandlePublishFailure() {
        // given
        String topic = "sports-scores";
        String key = "123";
        ScoreUpdate payload = new ScoreUpdate("123", "0:0");

        CompletableFuture<SendResult<String, String>> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("Kafka unavailable"));

        when(kafkaTemplate.send(anyString(), anyString())).thenReturn(failedFuture);

        // when
        publisherService.publish(key, payload);

        verify(kafkaTemplate, times(1)).send(topic, "{\"eventId\":\"123\",\"currentScore\":\"0:0\"}");
    }
}
