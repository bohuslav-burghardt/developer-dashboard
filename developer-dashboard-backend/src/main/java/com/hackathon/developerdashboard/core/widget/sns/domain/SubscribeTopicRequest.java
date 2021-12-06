package com.hackathon.developerdashboard.core.widget.sns.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class SubscribeTopicRequest {

    private String region;
    @NotBlank
    private String topicName;
    @NotNull
    private Protocol protocol;
    @NotBlank
    private String endpoint;

}
