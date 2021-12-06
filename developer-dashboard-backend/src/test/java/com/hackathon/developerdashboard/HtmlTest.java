package com.hackathon.developerdashboard;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

public class HtmlTest extends BaseTest {
    @Test
    public void x() {
      Assert.isTrue(trt.getForObject("http://localhost:" + port + "/", String.class).contains("<meta charset=\"utf-8\">"), "HTML must contain meta charset!");
    }
}
