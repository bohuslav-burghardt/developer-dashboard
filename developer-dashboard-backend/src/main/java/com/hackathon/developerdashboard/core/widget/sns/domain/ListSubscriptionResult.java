package com.hackathon.developerdashboard.core.widget.sns.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ListSubscriptionResult {

    private List<Subscription> subscriptions;

    @Data
    @Accessors(chain = true)
    public static class Subscription {
        private String subscriptionArn;
        private String topicArn;
        private String endpoint;
        private Protocol protocol;
        private String region;
        private String topicName;
    }

}
