package com.hackathon.developerdashboard.core;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AwsUtilsTest {

    @Test
    void testGetQueueNameFromArn() {
        String arn = "arn:aws:sqs:ap-southeast-1:278818144798:core-sgp-test-automation-queue-ies-callback";
        assertThat(AwsUtils.getQueueNameFromArn(arn)).isEqualTo("core-sgp-test-automation-queue-ies-callback");
    }

    @Test
    void getGetRegionFromArn() {
        String arn = "arn:aws:sqs:ap-southeast-1:278818144798:core-sgp-test-automation-queue-ies-callback";
        assertThat(AwsUtils.getRegionFromArn(arn)).isEqualTo("ap-southeast-1");
    }

}