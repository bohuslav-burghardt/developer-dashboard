package com.hackathon.developerdashboard.core.configuration.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class UserConfiguration {

    private String defaultEmail;
    private JiraConfig jira = new JiraConfig();
    private List<WidgetDefinition> widgets = new ArrayList<>();

}
