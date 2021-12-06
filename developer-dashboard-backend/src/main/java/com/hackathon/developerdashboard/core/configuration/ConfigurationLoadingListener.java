package com.hackathon.developerdashboard.core.configuration;

import com.google.gson.Gson;
import com.hackathon.developerdashboard.core.domain.UserConfiguration;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
