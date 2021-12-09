package com.hackathon.developerdashboard.core;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import org.springframework.util.StringUtils;

public class AwsUtils {

    public static String getQueueNameFromArn(String arn) {
        if (!StringUtils.hasText(arn)) {
            return null;
        }
        String[] arnParts = arn.split(":");
        if (!"sqs".equalsIgnoreCase(arnParts[2])) {
            throw new IllegalArgumentException("Not SQS ARN: " + arn);
        }
        return arnParts[5];
    }

    public static String getRegionFromArn(String arn) {
        if (!StringUtils.hasText(arn)) {
            return null;
        }
        String[] arnParts = arn.split(":");
        if (!"sqs".equalsIgnoreCase(arnParts[2])) {
            throw new IllegalArgumentException("Not SQS ARN: " + arn);
        }
        return arnParts[3];
    }

    public static AmazonSQS createSqsClient(String region) {
        if (!StringUtils.hasText(region)) {
            return AmazonSQSClient.builder().build();
        }
        return AmazonSQSClient.builder().withRegion(region).build();
    }

    public static AmazonSNS createSnsClient(String region) {
        if (!StringUtils.hasText(region)) {
            return AmazonSNSClientBuilder.defaultClient();
        }
        return AmazonSNSClient.builder().withRegion(region).build();
    }

}
