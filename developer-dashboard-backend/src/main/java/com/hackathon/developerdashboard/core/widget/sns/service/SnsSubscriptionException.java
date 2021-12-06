package com.hackathon.developerdashboard.core.widget.sns.service;

import com.amazonaws.services.sns.model.Topic;

import java.util.List;
import java.util.stream.Collectors;

public class SnsSubscriptionException extends RuntimeException {

    public SnsSubscriptionException(String message) {
        super(message);
    }

    public static SnsSubscriptionException noTopicFound(String name) {
        return new SnsSubscriptionException("No topic was found for name " + name);
    }

    public static SnsSubscriptionException tooManyTopics(List<Topic> topics) {
        return new SnsSubscriptionException("Too many matching SNS topics were found: " +
                topics.stream().map(Topic::getTopicArn).collect(Collectors.toList()));
    }

}
