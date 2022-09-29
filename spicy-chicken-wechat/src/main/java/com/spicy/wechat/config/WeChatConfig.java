package com.spicy.wechat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "we-chat")
public class WeChatConfig {

    private String appId;

    private String appSecret;

    private String token;
}
