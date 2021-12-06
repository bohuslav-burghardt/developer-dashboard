package com.hackathon.developerdashboard.core.widget.sns.api;

import com.hackathon.developerdashboard.core.widget.sns.domain.ListSubscriptionResult;
import com.hackathon.developerdashboard.core.widget.sns.domain.Protocol;
import com.hackathon.developerdashboard.core.widget.sns.domain.SubscribeTopicRequest;
import com.hackathon.developerdashboard.core.widget.sns.domain.SubscribeTopicResult;
import com.hackathon.developerdashboard.core.widget.sns.domain.UnsubscribeTopicRequest;
import com.hackathon.developerdashboard.core.widget.sns.domain.UnsubscribeTopicResult;
import com.hackathon.developerdashboard.core.widget.sns.service.SnsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class SnsSubscriptionController {

    private final SnsService snsService;

    @GetMapping("/api/widgets/sns-subscription")
    public ListSubscriptionResult listSubscriptions(
            @RequestParam("topicName") String topicName,
            @RequestParam(value = "region", required = false) String region,
            @RequestParam(value = "protocol", required = false) Protocol protocol,
            @RequestParam(value = "endpoint", required = false) String endpoint
    ) {
        return snsService.listSubscriptions(region, topicName, protocol, endpoint);
    }

    @PostMapping("/api/widgets/sns-subscription")
    public SubscribeTopicResult subscribe(@RequestBody @Valid SubscribeTopicRequest request) {
        return snsService.subscribe(request);
    }

    @DeleteMapping("/api/widgets/sns-subscription")
    public void unsubscribe(@RequestBody @Valid UnsubscribeTopicRequest request) {
        snsService.unsubscribe(request);
    }

}
