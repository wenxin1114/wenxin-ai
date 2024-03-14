package com.wenxin.ai.config.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "model.spark")
public class SparkConfig {
    private String hostUrl;
    private String appid;
    private String apiSecret;
    private String apiKey;
}
