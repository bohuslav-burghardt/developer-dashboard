package com.hackathon.developerdashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public  abstract class BaseTest {
    @LocalServerPort
    protected int port;

    @Autowired
    protected TestRestTemplate trt;
}
