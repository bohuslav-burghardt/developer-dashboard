package com.hackathon.developerdashboard.core.configuration.service;

import com.hackathon.developerdashboard.core.configuration.domain.UserConfiguration;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ConfigurationHolder {

    private UserConfiguration userConfiguration;

}
