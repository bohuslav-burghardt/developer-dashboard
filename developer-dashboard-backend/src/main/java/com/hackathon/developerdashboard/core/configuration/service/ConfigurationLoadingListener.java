package com.hackathon.developerdashboard.core.configuration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConfigurationLoadingListener {

    private final ConfigurationService configurationService;

    @EventListener(ContextRefreshedEvent.class)
    public void handleEvent() {
        configurationService.refreshConfiguration();
    }

}
