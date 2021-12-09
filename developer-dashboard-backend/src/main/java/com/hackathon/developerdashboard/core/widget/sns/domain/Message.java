package com.hackathon.developerdashboard.core.widget.sns.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Message {

    private String messageId;
    private String body;

}
