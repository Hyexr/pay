package com.example.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 黄烨轩
 * @version : 1.0
 * @Project : pay
 * @Package : com.example.pay.config
 * @ClassName : AliAccountConfig.java
 * @createTime : 2023/6/15 16:45
 */
@Component
@ConfigurationProperties(prefix = "ali")
@Data
public class AliAccountConfig {
    private String appId;
    private String privateKey;
    private String publicKey;
    private String notifyUrl;
    private String returnUrl;
}
