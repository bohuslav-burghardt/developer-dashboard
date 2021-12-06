package com.hackathon.developerdashboard.core.configuration.service;

import com.google.gson.Gson;
import com.hackathon.developerdashboard.core.configuration.domain.UserConfiguration;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfigurationService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Gson gson;
    private final ConfigurationLoader configurationLoader;
    private final ConfigurationHolder configurationHolder;

    public UserConfiguration getUserConfiguration() {
        return configurationHolder.getUserConfiguration();
    }

    public void refreshConfiguration() {
        UserConfiguration userConfiguration = configurationLoader.loadConfiguration();
        configurationHolder.setUserConfiguration(userConfiguration);
        logger.info("Loaded user configuration {}.", gson.toJson(userConfiguration));
    }

}
