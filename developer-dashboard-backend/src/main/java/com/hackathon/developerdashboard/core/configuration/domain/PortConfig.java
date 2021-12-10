package com.hackathon.developerdashboard.core.configuration.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class PortConfig {
    private int port;
}
