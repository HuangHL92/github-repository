package com.ruoyi.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @author Binary Wang
 *
 */
@Configuration
@Getter
public class WxMpConfig {
  @Value("${jiyun.wechat.token}")
  private String token;

  @Value("${jiyun.wechat.appid}")
  private String appid;

  @Value("${jiyun.wechat.appsecret}")
  private String appsecret;

  @Value("${jiyun.wechat.aeskey}")
  private String aesKey;

  @Value("${jiyun.webPath}")
  private String webPath;

}
