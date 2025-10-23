package com.sport.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
class EventService {

    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    private final TaskScheduler scheduler;
    private final EventScheduler eventScheduler;

    public EventService(TaskScheduler scheduler, EventScheduler eventScheduler) {
        this.scheduler = scheduler;
        this.eventScheduler = eventScheduler;
    }

    public void updateStatus(String eventId, boolean isAlive) {
        if (isAlive) {
            if (!scheduledTasks.containsKey(eventId)) {
                log.info("Scheduling polling for eventId={}", eventId);
                ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(
                        () -> eventScheduler.fetchAndPublishScore(eventId),
                        Duration.ofSeconds(10)
                );
                scheduledTasks.put(eventId, future);
            }
        } else {
            ScheduledFuture<?> future = scheduledTasks.remove(eventId);
            if (future != null) {
                log.info("Cancelled polling for not-live eventId={}", eventId);
                future.cancel(true);
            } else {
                log.info("No active task to cancel for eventId={}", eventId);
            }
        }
    }
}
