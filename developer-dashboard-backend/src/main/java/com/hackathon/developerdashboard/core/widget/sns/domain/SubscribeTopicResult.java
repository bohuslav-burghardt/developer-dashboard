package com.hackathon.developerdashboard.core.widget.sns.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class SubscribeTopicResult {

    private String region;
    private String topicArn;
    private Set<String> subscriptionArns;
    private String createdQueueArn;
    private boolean wasAlreadySubscribed;

}
