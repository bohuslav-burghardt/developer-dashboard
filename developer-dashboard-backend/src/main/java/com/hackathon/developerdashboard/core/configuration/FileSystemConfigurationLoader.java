package com.hackathon.developerdashboard.core.configuration;

import com.google.gson.Gson;
import com.hackathon.developerdashboard.configuration.UserConfigProperties;
import com.hackathon.developerdashboard.core.domain.UserConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileSystemConfigurationLoader implements ConfigurationLoader {

    private final Gson gson;
    private final ResourceLoader resourceLoader;
    private final UserConfigProperties userConfigProperties;

    @Override
    public UserConfiguration loadConfiguration() {
        String location = userConfigProperties.getLocation();
        
        log.info("loading user config from <{}>", location);
        
        try {
            return doLoadConfig(location);
        } catch (IOException e) {
            throw new ConfigurationLoadingException("Failed to load configuration " + location, e);
        }
    }

    private UserConfiguration doLoadConfig(String location) throws IOException {
        Resource resource = resourceLoader.getResource(location);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String content = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            return gson.fromJson(content, UserConfiguration.class);
        }
    }

}
