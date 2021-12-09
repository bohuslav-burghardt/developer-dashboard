package com.hackathon.developerdashboard.core.calendar;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CalendarController {
    private final CalendarService calendarService;

    @GetMapping("api/ro-calendar/ro-events")
    public EventListResponse get() throws Exception {
        return new EventListResponse().setEvents(calendarService.getEvents());
    }

    @PostMapping("api/ro-calendar/ro-event/{eventId}:reserve")
    public void reserve(
            @PathVariable String eventId,
            @RequestBody UpdateEventRequest updateEventRequest
    ) throws Exception {
         calendarService.reserve(eventId, updateEventRequest.getDescription());
    }
}