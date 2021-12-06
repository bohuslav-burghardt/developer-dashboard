package com.hackathon.developerdashboard.core;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import org.springframework.util.StringUtils;

public class AwsUtils {

    public static AmazonSNS createSnsClient(String region) {
        if (!StringUtils.hasText(region)) {
            return AmazonSNSClientBuilder.defaultClient();
        }
        return AmazonSNSClient.builder().withRegion(region).build();
    }

}
