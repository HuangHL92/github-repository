package com.ruoyi.common.utils.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * sms configuration
 *
 * @author yufei
 */
@Component
@Configuration
@EnableConfigurationProperties(SmsProperties.class)
public class SmsConfiguration {

    @Autowired
    public static SmsProperties properties;


    @Autowired
    public SmsConfiguration( SmsProperties properties) {

        this.properties = properties;
    }


    public static SmsProperties getProperties() {
        return properties;
    }
}
