package com.hackathon.developerdashboard.core.widget.sns.api;

import com.hackathon.developerdashboard.core.widget.sns.domain.SubscribeTopicRequest;
import com.hackathon.developerdashboard.core.widget.sns.domain.SubscribeTopicResult;
import com.hackathon.developerdashboard.core.widget.sns.domain.UnsubscribeTopicRequest;
import com.hackathon.developerdashboard.core.widget.sns.service.SnsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class SnsSubscriptionController {

    private final SnsService snsService;

    @PostMapping("/api/widgets/sns-subscription")
    public SubscribeTopicResult subscribe(@RequestBody @Valid SubscribeTopicRequest request) {
        return snsService.subscribe(request);
    }

    @DeleteMapping("/api/widgets/sns-subscription")
    public SubscribeTopicResult unsubscribe(@RequestBody @Valid UnsubscribeTopicRequest request) {
        return snsService.unsubscribe(request);
    }

}
