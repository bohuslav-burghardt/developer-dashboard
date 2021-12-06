package com.hackathon.developerdashboard.core.configuration.service;

import com.hackathon.developerdashboard.core.configuration.domain.UserConfiguration;

public interface ConfigurationLoader {

    UserConfiguration loadConfiguration();

}
