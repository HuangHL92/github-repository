package com.ruoyi.common.utils.sms;


import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * sms properties
 *
 * @author yufei
 */
@Data
@ConfigurationProperties(prefix = "sms")
public class SmsProperties {

    private List<SmsConfig> configs;

    @Data
    public static class SmsConfig {
        /**
         * 通道
         */
        private String channel;

        /**
         * 地址
         */
        private String url;

        /**
         * 账号
         */
        private String account;

        /**
         * 密码
         */
        private String password;

        /**
         * 签名
         */
        private String sign;

        /**
         * 说明
         */
        private String remark;


        /**
         * 是否启用
         */
        private boolean enable;

    }

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
}
