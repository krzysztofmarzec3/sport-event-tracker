package com.sport.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
@Service
class EventScheduler {

    private final WebClient webClient;
    private final PublisherService publisher;

    public EventScheduler(WebClient.Builder builder, PublisherService publisher) {
        this.webClient = builder.baseUrl("http://localhost:8080").build();
        this.publisher = publisher;
    }

    public void fetchAndPublishScore(String eventId) {
        webClient.get()
                .uri("/score/" + eventId)
                .retrieve()
                .bodyToMono(ScoreUpdate.class)
                .retryWhen(Retry.backoff(3, Duration.ofMillis(300))
                        .filter(err -> !(err instanceof IllegalArgumentException)))
                .doOnError(e -> log.error("Error fetching score for event {}", eventId, e))
                .subscribe(score -> {
                    String message = String.format("Event %s: Score %s", score.getEventId(), score.getCurrentScore());
                    publisher.publish(eventId, score);
                    log.info("Published: {}", message);
                });
    }
}
