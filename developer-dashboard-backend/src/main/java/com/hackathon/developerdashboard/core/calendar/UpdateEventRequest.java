package com.hackathon.developerdashboard.core.calendar;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UpdateEventRequest {
    private String description;
}
