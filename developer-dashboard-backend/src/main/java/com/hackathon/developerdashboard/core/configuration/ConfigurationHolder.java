package com.hackathon.developerdashboard.core.configuration;

import com.hackathon.developerdashboard.core.domain.UserConfiguration;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ConfigurationHolder {

    private UserConfiguration userConfiguration;

}
