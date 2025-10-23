package com.sport.task;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PollingTaskTest {
    @Autowired EventService manager;

    @Test
    void schedulesAndCancels() throws InterruptedException {
        manager.updateStatus("e2", true);
        Thread.sleep(1200); // allow schedule to start
        manager.updateStatus("e2", false);
    }
}
