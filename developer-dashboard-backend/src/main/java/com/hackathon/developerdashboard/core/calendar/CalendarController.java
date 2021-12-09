package com.hackathon.developerdashboard.core.calendar;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class CalendarController {
    private final HttpServletRequest httpServletRequest;
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
    
    @GetMapping("api/ro-calender:check-auth")
    public AuthCheckResult checkCalendarAuth() throws Exception {
        return calendarService.checkCalendarAuth();
    }
    @GetMapping("api/ro-calender/callback")
    public RedirectView  receiveGoogleAuthCallback(RedirectAttributes attributes) throws Exception {
        calendarService.receiveCallback(httpServletRequest.getParameter("code"));

        attributes.addFlashAttribute("flashAttribute", "redirectWithRedirectView");
        attributes.addAttribute("attribute", "redirectWithRedirectView");
        return new RedirectView("http://localhost:4200/ro-calendar");
    }
}