package com.sport.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.scheduling.TaskScheduler;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventServiceTest {

    private TaskScheduler scheduler;
    private EventScheduler eventScheduler;
    private EventService eventService;
    private ScheduledFuture<?> scheduledFuture;

    @BeforeEach
    void setUp() {
        scheduler = mock(TaskScheduler.class);
        eventScheduler = mock(EventScheduler.class);
        scheduledFuture = mock(ScheduledFuture.class);

        // domyślnie scheduler zwraca atrapę future
        doReturn(scheduledFuture)
                .when(scheduler)
                .scheduleAtFixedRate(any(Runnable.class), any(Duration.class));

        eventService = new EventService(scheduler, eventScheduler);
    }

    @Test
    void shouldScheduleTaskWhenEventGoesLive() {
        // when
        eventService.updateStatus("E1", true);

        // then
        verify(scheduler).scheduleAtFixedRate(any(Runnable.class), eq(Duration.ofSeconds(10)));
    }

    @Test
    void shouldNotScheduleTwiceForSameEvent() {
        // given
        eventService.updateStatus("E1", true);

        // when
        eventService.updateStatus("E1", true);

        // then
        // scheduler wywołany tylko raz
        verify(scheduler, times(1)).scheduleAtFixedRate(any(Runnable.class), any(Duration.class));
    }

    @Test
    void shouldCancelTaskWhenEventStops() {
        // given
        eventService.updateStatus("E1", true);

        // when
        eventService.updateStatus("E1", false);

        // then
        verify(scheduledFuture).cancel(true);
    }

    @Test
    void shouldNotFailWhenCancellingNonExistingTask() {
        // when
        eventService.updateStatus("E2", false);

        // then
        // brak wywołań cancel
        verify(scheduledFuture, never()).cancel(anyBoolean());
    }

    @Test
    void scheduledRunnableShouldInvokeEventScheduler() {
        // given
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        doReturn(scheduledFuture)
                .when(scheduler)
                .scheduleAtFixedRate(runnableCaptor.capture(), any(Duration.class));

        eventService.updateStatus("E3", true);

        // when – uruchamiamy ręcznie przechwycony runnable
        runnableCaptor.getValue().run();

        // then
        verify(eventScheduler).fetchAndPublishScore("E3");
    }
}
