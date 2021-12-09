package com.hackathon.developerdashboard.core.calendar;

import com.google.api.services.calendar.model.Event;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class EventListResponse {
    private List<Event> events;
}
