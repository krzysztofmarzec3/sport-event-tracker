package com.sport.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventStatusRequest {
    @NotBlank(message = "eventId is required")
    private String eventId;

    @NotNull(message = "status is required")
    private Boolean live; // true = live, false = not live
}