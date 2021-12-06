package com.hackathon.developerdashboard.core.configuration.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class JiraConfig {

    private String personalToken;

}
