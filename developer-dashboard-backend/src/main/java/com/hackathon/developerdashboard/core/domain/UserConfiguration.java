package com.hackathon.developerdashboard.core.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class UserConfiguration {

    private String defaultEmail;

}
