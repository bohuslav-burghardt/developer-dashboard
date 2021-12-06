package com.hackathon.developerdashboard.core.configuration.api;

import com.hackathon.developerdashboard.core.configuration.domain.UserConfiguration;
import com.hackathon.developerdashboard.core.configuration.service.ConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ConfigurationController {

    private final ConfigurationService configurationService;

    @GetMapping("/api/configuration")
    public UserConfiguration userConfiguration() {
        return configurationService.getUserConfiguration();
    }

    @PostMapping("/api/configuration/reload")
    public void reloadConfiguration() {
        configurationService.refreshConfiguration();
    }

}
