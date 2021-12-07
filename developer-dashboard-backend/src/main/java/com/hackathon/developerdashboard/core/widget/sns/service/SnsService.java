package com.hackathon.developerdashboard.core.widget.sns.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.InvalidParameterException;
import com.amazonaws.services.sns.model.ListSubscriptionsByTopicResult;
import com.amazonaws.services.sns.model.ListTopicsResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.amazonaws.services.sns.model.Subscription;
import com.amazonaws.services.sns.model.Topic;
import com.hackathon.developerdashboard.core.AwsUtils;
import com.hackathon.developerdashboard.core.widget.sns.domain.ListSubscriptionResult;
import com.hackathon.developerdashboard.core.widget.sns.domain.Protocol;
import com.hackathon.developerdashboard.core.widget.sns.domain.SubscribeTopicRequest;
import com.hackathon.developerdashboard.core.widget.sns.domain.SubscribeTopicResult;
import com.hackathon.developerdashboard.core.widget.sns.domain.UnsubscribeTopicRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
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

        String topicArn = getTopicByName(amazonSNS, request.getTopicName()).getTopicArn();

        Set<String> existingSubscriptionArns = listSubscriptionsWithPaging(amazonSNS, topicArn)
                .stream()
                .filter(subscription -> request.getProtocol().getAwsValue().equals(subscription.getProtocol()) &&
                        request.getEndpoint().equals(subscription.getEndpoint()))
                .map(Subscription::getSubscriptionArn)
                .collect(Collectors.toSet());

        if (!existingSubscriptionArns.isEmpty()) {
            return new SubscribeTopicResult()
                    .setRegion(request.getRegion())
                    .setTopicArn(topicArn)
                    .setSubscriptionArns(existingSubscriptionArns)
                    .setWasAlreadySubscribed(true);
        }

        SubscribeRequest snsSubscribeRequest = new SubscribeRequest();
        snsSubscribeRequest.setEndpoint(request.getEndpoint());
        snsSubscribeRequest.setProtocol(request.getProtocol().getAwsValue());
        snsSubscribeRequest.setTopicArn(topicArn);

        SubscribeResult subscribe = amazonSNS.subscribe(snsSubscribeRequest);

        return new SubscribeTopicResult()
                .setRegion(request.getRegion())
                .setTopicArn(topicArn)
                .setSubscriptionArns(Collections.singleton(subscribe.getSubscriptionArn()))
                .setWasAlreadySubscribed(false);
    }

    public void unsubscribe(UnsubscribeTopicRequest request) {
        AmazonSNS amazonSNS = AwsUtils.createSnsClient(request.getRegion());
        try {
            amazonSNS.unsubscribe(request.getSubscriptionArn());
        }catch(InvalidParameterException ex ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "InvalidParameterException, with message<"+ex.getMessage()+"> is your subscriptionArn still pending? Please accept it in your email application and then delete it here.", ex);
        }
    }

    public ListSubscriptionResult listSubscriptions(String region, String topicName, Protocol protocol, String endpoint) {
        AmazonSNS amazonSNS = AwsUtils.createSnsClient(region);
        String topicArn = getTopicByName(amazonSNS, topicName).getTopicArn();

        List<ListSubscriptionResult.Subscription> subscriptions = listSubscriptionsWithPaging(amazonSNS, topicArn)
                .stream()
                .map(snsSubscription -> new ListSubscriptionResult.Subscription()
                        .setTopicName(topicName)
                        .setRegion(region)
                        .setTopicArn(topicArn)
                        .setSubscriptionArn(snsSubscription.getSubscriptionArn())
                        .setProtocol(Protocol.fromAwsValue(snsSubscription.getProtocol()))
                        .setEndpoint(snsSubscription.getEndpoint()))
                .filter(subscription -> {
                    if (protocol != null && protocol != subscription.getProtocol()) {
                        return false;
                    }
                    if (StringUtils.hasText(endpoint) && !endpoint.equals(subscription.getEndpoint())) {
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());

        return new ListSubscriptionResult().setSubscriptions(subscriptions);
    }

    //~ Util functions

    private Topic getTopicByName(AmazonSNS sns, String topicName) {
        ListTopicsResult listTopicsResult = sns.listTopics();

        List<Topic> topics = listTopicsResult
                .getTopics()
                .stream()
                .filter(topic -> topic.getTopicArn().endsWith("-" + topicName))
                .collect(Collectors.toList());

        if (topics.isEmpty()) {
            throw noTopicFound(topicName);
        }
        if (topics.size() > 1) {
            throw tooManyTopics(topics);
        }
        return topics.get(0);
    }

    private List<Subscription> listSubscriptionsWithPaging(AmazonSNS sns, String topicArn) {
        ListSubscriptionsByTopicResult result = sns.listSubscriptionsByTopic(topicArn);
        List<Subscription> subscriptions = new ArrayList<>(result.getSubscriptions());

        while (result.getNextToken() != null) {
            result = sns.listSubscriptionsByTopic(topicArn, result.getNextToken());
            subscriptions.addAll(result.getSubscriptions());
        }
        return subscriptions;
    }

}
