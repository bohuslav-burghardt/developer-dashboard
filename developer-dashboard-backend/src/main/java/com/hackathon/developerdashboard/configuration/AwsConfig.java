package com.hackathon.developerdashboard.configuration;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

    @Bean
    public AmazonSNS amazonSNS() {
        return AmazonSNSClientBuilder.defaultClient();
    }

}
