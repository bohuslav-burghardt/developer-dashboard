package com.hackathon.developerdashboard.core.calendar;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class AuthCheckResult {
    private String authUrl;
    private AuthCheckState authCheckState;
    
    enum AuthCheckState {
        OK,
        AUTH_REQUIRED
    }
}
