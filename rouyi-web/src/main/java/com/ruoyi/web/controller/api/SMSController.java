package com.ruoyi.web.controller.api;

import cn.hutool.core.util.RandomUtil;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("短信接口服务")
@RestController
@RequestMapping("/api/sms/*")
public class SMSController {

    @ApiOperation("发送验证码")
    @PostMapping("sendVcode")
    public AjaxResult sendVcode(String mobile,String account,String password)
    {
        //1.参数验证
        if(StringUtils.isEmpty(account) || StringUtils.isEmpty(password) || StringUtils.isEmpty(mobile)) {
            return AjaxResult.error("参数错误");
        }
        //TODO 2.验证账号密码

        //3.生成六位随机数
        String vcode = RandomUtil.randomNumbers(6);

        //TODO 4.发送短信
        return AjaxResult.success(vcode);
    }

    @ApiOperation("发送短信（单条）")
    @PostMapping("sendSms")
    public AjaxResult sendSms(String mobile,String content, String account,String password)
    {
        //1.参数验证
        if(StringUtils.isEmpty(account) || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(mobile) || StringUtils.isEmpty(content) ) {
            return AjaxResult.error("参数错误");
        }
        //TODO 2.验证账号密码

        //TODO 3.发送短信

        return AjaxResult.success();
    }
}
