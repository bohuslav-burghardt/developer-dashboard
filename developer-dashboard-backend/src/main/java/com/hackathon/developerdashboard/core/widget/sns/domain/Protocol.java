package com.hackathon.developerdashboard.core.widget.sns.domain;

public enum Protocol {
    EMAIL("email");

    private final String awsValue;

    Protocol(String awsValue) {
        this.awsValue = awsValue;
    }

    public String getAwsValue() {
        return awsValue;
    }
}
