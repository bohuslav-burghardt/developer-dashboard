package com.hackathon.developerdashboard.core.widget.sns.domain;

public enum Protocol {
    EMAIL("email"),
    SQS("sqs");

    private final String awsValue;

    Protocol(String awsValue) {
        this.awsValue = awsValue;
    }

    public String getAwsValue() {
        return awsValue;
    }

    public static Protocol fromAwsValue(String awsValue) {
        for (Protocol protocol : values()) {
            if (protocol.getAwsValue().equalsIgnoreCase(awsValue)) {
                return protocol;
            }
        }
        return null;
    }

}
