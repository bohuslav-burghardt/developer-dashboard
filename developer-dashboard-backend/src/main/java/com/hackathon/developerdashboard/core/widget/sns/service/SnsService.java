package com.hackathon.developerdashboard.core.widget.sns.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.GetSubscriptionAttributesResult;
import com.amazonaws.services.sns.model.InvalidParameterException;
import com.amazonaws.services.sns.model.ListSubscriptionsByTopicResult;
import com.amazonaws.services.sns.model.ListTopicsResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.amazonaws.services.sns.model.Subscription;
import com.amazonaws.services.sns.model.TagResourceRequest;
import com.amazonaws.services.sns.model.Topic;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.QueueAttributeName;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.hackathon.developerdashboard.core.AwsUtils;
import com.hackathon.developerdashboard.core.widget.sns.domain.ListSubscriptionResult;
import com.hackathon.developerdashboard.core.widget.sns.domain.Message;
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
import java.util.UUID;
import java.util.stream.Collectors;

import static com.amazonaws.services.sqs.model.QueueAttributeName.QueueArn;
import static com.hackathon.developerdashboard.core.widget.sns.service.SnsSubscriptionException.noTopicFound;
import static com.hackathon.developerdashboard.core.widget.sns.service.SnsSubscriptionException.tooManyTopics;

@Service
@RequiredArgsConstructor
public class SnsService {

    public SubscribeTopicResult subscribe(SubscribeTopicRequest request) {
        if (request.getProtocol() == Protocol.EMAIL && !StringUtils.hasText(request.getEndpoint())) {
            throw new IllegalArgumentException("Endpoint must be specified for EMAIL subscription.");
        }
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

        String sqsArn = null;
        String endpoint = request.getEndpoint();
        if (request.getProtocol() == Protocol.SQS) {
            AmazonSQS amazonSQS = AwsUtils.createSqsClient(request.getRegion());
            String queueUrl = amazonSQS.createQueue("dev-dashboard-" + UUID.randomUUID()).toString();
            sqsArn = amazonSQS.getQueueAttributes(queueUrl,
                    Collections.singletonList(QueueArn.toString())).getAttributes().get(QueueArn.toString());
            endpoint = sqsArn;
        }

        SubscribeRequest snsSubscribeRequest = new SubscribeRequest();
        snsSubscribeRequest.setEndpoint(endpoint);
        snsSubscribeRequest.setProtocol(request.getProtocol().getAwsValue());
        snsSubscribeRequest.setTopicArn(topicArn);

        SubscribeResult subscribe = amazonSNS.subscribe(snsSubscribeRequest);

        return new SubscribeTopicResult()
                .setRegion(request.getRegion())
                .setTopicArn(topicArn)
                .setSubscriptionArns(Collections.singleton(subscribe.getSubscriptionArn()))
                .setCreatedQueueArn(sqsArn)
                .setWasAlreadySubscribed(false);
    }

    public void unsubscribe(UnsubscribeTopicRequest request) {
        AmazonSNS amazonSNS = AwsUtils.createSnsClient(request.getRegion());
        try {
            amazonSNS.unsubscribe(request.getSubscriptionArn());
        } catch (InvalidParameterException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "InvalidParameterException, with message<" + ex.getMessage() + ">\nis your subscriptionArn still pending? Please accept it in your email application and then delete it here.", ex);
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

    public List<Message> getMessages(String endpointArn) {
        AmazonSQS sqsClient = AwsUtils.createSqsClient(AwsUtils.getRegionFromArn(endpointArn));
        String queueName = AwsUtils.getQueueNameFromArn(endpointArn);

        String queueUrl = sqsClient.getQueueUrl(queueName).getQueueUrl();
        ReceiveMessageResult receiveMessageResult = sqsClient.receiveMessage(queueUrl);

        List<Message> messages = new ArrayList<>();
        receiveMessageResult.getMessages().forEach(message -> {
            sqsClient.deleteMessage(queueUrl, message.getReceiptHandle());
            messages.add(new Message().setBody(message.getBody()).setMessageId(message.getMessageId()));
        });
        return messages;
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
