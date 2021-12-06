package com.hackathon.developerdashboard.core.configuration.service;

import com.hackathon.developerdashboard.core.configuration.domain.UserConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

// Fallback implementation
@Component
@ConditionalOnMissingBean(FileSystemConfigurationLoader.class)
public class InMemoryConfigurationLoader implements ConfigurationLoader {

    public UserConfiguration loadConfiguration() {
        return new UserConfiguration();
    }

}
