package com.hackathon.developerdashboard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class UiController {
    private final HttpServletResponse httpServletResponse;
    
    
    public byte[] html() {
        return null;
    }
}
