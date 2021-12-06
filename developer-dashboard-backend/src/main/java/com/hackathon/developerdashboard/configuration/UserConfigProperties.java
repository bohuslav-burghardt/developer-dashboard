package com.hackathon.developerdashboard.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Data
@Component
@ConfigurationProperties("user-configuration")
public class UserConfigProperties {

    private Path location;

}
