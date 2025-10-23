package com.sport.task;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ThreadLocalRandom;

@RestController
public class MockExternalApiController {
    @GetMapping("/score/{eventId}")
    public ResponseEntity<ScoreUpdate> getScore(@PathVariable String eventId) {
        int a = ThreadLocalRandom.current().nextInt(0, 5);
        int b = ThreadLocalRandom.current().nextInt(0, 5);
        return ResponseEntity.ok(new ScoreUpdate(eventId, a + ":" + b));
    }
}
