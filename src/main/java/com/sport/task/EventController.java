package com.sport.task;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/status")
    ResponseEntity<String> updateStatus(@Valid @RequestBody EventStatusRequest request) {
        boolean live = Boolean.TRUE.equals(request.getLive());
        eventService.updateStatus(request.getEventId(), live);
        return ResponseEntity.ok("Status updated");
    }
}