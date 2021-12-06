package com.hackathon.developerdashboard.api.controller;

import com.hackathon.developerdashboard.core.configuration.ConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ConfigurationReloadController {

    private final ConfigurationService configurationService;

    @PostMapping("/api/configuration/reload")
    public void reloadConfiguration() {
        configurationService.refreshConfiguration();
    }

}
