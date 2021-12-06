package com.hackathon.developerdashboard.core.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.UUID;

@Data
@Accessors(fluent = true)
public class WidgetDefinition {

    private UUID widgetDefinitionId;
    private String widgetId;
    private Map<String, Object> configuration;

}
