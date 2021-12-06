package com.hackathon.developerdashboard.core.configuration;

import com.google.gson.Gson;
import com.hackathon.developerdashboard.configuration.UserConfigProperties;
import com.hackathon.developerdashboard.core.domain.UserConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FileSystemUserConfigurationLoader implements UserConfigurationLoader {

    private final Gson gson;
    private final ResourceLoader resourceLoader;
    private final UserConfigProperties userConfigProperties;

    @Override
    public UserConfiguration loadConfiguration() {
        try {
            return doLoadConfig(userConfigProperties.getLocation());
        } catch (IOException e) {
            // TODO better error handling
            throw new IllegalArgumentException("Failed to load configuration " + userConfigProperties.getLocation());
        }
    }

    private UserConfiguration doLoadConfig(Path location) throws IOException {
        Resource resource = resourceLoader.getResource(location.toString());
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String content = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            return gson.fromJson(content, UserConfiguration.class);
        }
    }

}
