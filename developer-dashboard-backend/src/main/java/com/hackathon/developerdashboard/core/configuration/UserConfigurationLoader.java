package com.hackathon.developerdashboard.core.configuration;

import com.hackathon.developerdashboard.core.domain.UserConfiguration;

public interface UserConfigurationLoader {

    UserConfiguration loadConfiguration();

}
