package com.hackathon.developerdashboard.core.widget.sns.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.ListTopicsResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.amazonaws.services.sns.model.Subscription;
import com.amazonaws.services.sns.model.Topic;
import com.hackathon.developerdashboard.core.AwsUtils;
import com.hackathon.developerdashboard.core.widget.sns.domain.SubscribeTopicResult;
import com.hackathon.developerdashboard.core.widget.sns.domain.SubscribeTopicRequest;
import com.hackathon.developerdashboard.core.widget.sns.domain.UnsubscribeTopicRequest;
import com.hackathon.developerdashboard.core.widget.sns.domain.UnsubscribeTopicResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.hackathon.developerdashboard.core.widget.sns.service.SnsSubscriptionException.noTopicFound;
import static com.hackathon.developerdashboard.core.widget.sns.service.SnsSubscriptionException.tooManyTopics;

@Service
@RequiredArgsConstructor
public class SnsService {

    public SubscribeTopicResult subscribe(SubscribeTopicRequest request) {
        AmazonSNS amazonSNS = AwsUtils.createSnsClient(request.getRegion());

        ListTopicsResult listTopicsResult = amazonSNS.listTopics();

        List<Topic> topics = listTopicsResult.getTopics().stream().filter(topic -> {
            return topic.getTopicArn().endsWith("-" + request.getTopicName());
        }).collect(Collectors.toList());

        if (topics.isEmpty()) {
            throw noTopicFound(request.getTopicName());
        }
        if (topics.size() > 1) {
            throw tooManyTopics(topics);
        }

        String topicArn = topics.get(0).getTopicArn();

        Set<String> existingSubscriptionArns = amazonSNS
                .listSubscriptionsByTopic(topicArn)
                .getSubscriptions()
                .stream()
                .filter(subscription -> {
                    return "email".equals(subscription.getProtocol()) &&
                            request.getEndpoint().equals(subscription.getEndpoint());
                })
                .map(Subscription::getSubscriptionArn)
                .collect(Collectors.toSet());

        if (!existingSubscriptionArns.isEmpty()) {
            SubscribeTopicResult result = new SubscribeTopicResult();
            result.setRegion(request.getRegion());
            result.setTopicArn(topicArn);
            result.setSubscriptionArns(existingSubscriptionArns);
            result.setWasAlreadySubscribed(true);
            return result;
        }

        SubscribeRequest snsSubscribeRequest = new SubscribeRequest();
        snsSubscribeRequest.setEndpoint(request.getEndpoint());
        snsSubscribeRequest.setProtocol("email");
        snsSubscribeRequest.setTopicArn(topicArn);

        SubscribeResult subscribe = amazonSNS.subscribe(snsSubscribeRequest);

        SubscribeTopicResult result = new SubscribeTopicResult();
        result.setRegion(request.getRegion());
        result.setTopicArn(topicArn);
        result.setSubscriptionArns(Collections.singleton(subscribe.getSubscriptionArn()));
        result.setWasAlreadySubscribed(false);
        return result;
    }

    public UnsubscribeTopicResult unsubscribe(UnsubscribeTopicRequest request) {
        return null; // TODO
    }

}
