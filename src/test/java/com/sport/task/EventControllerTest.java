package com.sport.task;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class EventControllerTest {
    @Autowired WebTestClient client;

    @Test
    void validatesAndUpdatesStatus() {
        EventStatusRequest req = new EventStatusRequest();
        req.setEventId("e1");
        req.setLive(true);

        client.post().uri("/events/status")
              .bodyValue(req)
              .exchange()
              .expectStatus().isOk();

        EventStatusRequest reqDead = new EventStatusRequest();
        reqDead.setEventId("e1");
        reqDead.setLive(false);

        client.post().uri("/events/status")
                .bodyValue(reqDead)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void rejectsInvalidPayload() {
        EventStatusRequest req = new EventStatusRequest();
        // missing fields
        client.post().uri("/events/status")
              .bodyValue(req)
              .exchange()
              .expectStatus().isBadRequest();
    }
}
