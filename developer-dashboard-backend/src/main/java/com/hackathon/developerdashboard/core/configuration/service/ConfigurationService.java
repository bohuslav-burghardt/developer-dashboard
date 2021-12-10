package com.hackathon.developerdashboard.core.configuration.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hackathon.developerdashboard.configuration.UserConfigProperties;
import com.hackathon.developerdashboard.core.configuration.domain.UserConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigurationService {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final ConfigurationLoader configurationLoader;
    private final ConfigurationHolder configurationHolder;
    private final UserConfigProperties userConfigProperties;
    
    // port is sent from frontend to backend, because backend creates redirects urls
    // port can depend on runtime environment (e.g. Intelli, Docker, etc.)
    private int port = 0;

    public UserConfiguration getUserConfiguration() {
        return configurationHolder.getUserConfiguration();
    }

    public void refreshConfiguration() {
        UserConfiguration userConfiguration = configurationLoader.loadConfiguration();
        configurationHolder.setUserConfiguration(userConfiguration);
        log.info("Loaded user configuration {}.", gson.toJson(userConfiguration));
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void update(UserConfiguration userConfiguration) throws IOException {
        String json = gson.toJson(userConfiguration);
        String location = userConfigProperties.getLocation();
        location = location.trim();
        if(location.startsWith("file:")) {
            location = location.substring(5);
        }
        if (location.endsWith("/") || location.endsWith("\\")) {
            location = location.substring(0, location.length() - 1);
        }

        File file2 = new File(location);
        if(file2.mkdirs()) {
            file2.delete();
        }
        file2.createNewFile();
        
        Files.write(Paths.get(location), json.getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
    }
}
