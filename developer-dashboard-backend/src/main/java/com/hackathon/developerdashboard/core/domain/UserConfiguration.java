package com.hackathon.developerdashboard.core.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(fluent = true)
public class UserConfiguration {

    private String defaultEmail;
    private JiraConfig jira;
    private List<WidgetDefinition> widgets;

}
