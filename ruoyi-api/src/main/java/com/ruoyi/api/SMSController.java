package com.ruoyi.api;

import com.ruoyi.base.ApiBaseController;
import com.ruoyi.common.annotation.ValidateRequest;
import com.ruoyi.common.base.ApiResult;
import com.ruoyi.common.utils.sms.SmsUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

@Api(value = "/sms", description = "短信接口服务")
@RestController
@RequestMapping("/api/sms/*")
public class SMSController extends ApiBaseController {


//    @ApiOperation("发送短信（单条）")
//    @PostMapping("send")
//    public ApiResult send(String mobile,String content, String account,String password)
//    {
//        //1.参数验证
//        if(StringUtils.isEmpty(account) || StringUtils.isEmpty(password)
//                || StringUtils.isEmpty(mobile) || StringUtils.isEmpty(content) ) {
//            return ApiResult.error("参数错误");
//        }
//        //TODO 2.验证账号密码
//
//
//        //TODO 3.发送短信
//        SmsUtils smsUtils = new SmsUtils();
//        String res = smsUtils.send(mobile,content);
//
//        //TODO 4.短信内容写入数据库
//
//        if("success".equals(res)) {
//            return ApiResult.success();
//        } else {
//            return ApiResult.error(res);
//        }
//
//    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    @ApiOperation(value="发送短信", notes="单条发送短信，请求头部需 x-access-token")
    @ValidateRequest
    public ApiResult send(String mobile,String content,@RequestHeader(value = "x-access-token") String token)
    {
        //1.参数验证
        if( StringUtils.isEmpty(mobile) || StringUtils.isEmpty(content) ) {
            return ApiResult.error("参数错误");
        }

        // 3.发送短信
        SmsUtils smsUtils = new SmsUtils();
        String res = smsUtils.send(mobile,content);

        //TODO 4.短信内容写入数据库

        if("success".equals(res)) {
            return ApiResult.success();
        } else {
            return ApiResult.error(res);
        }

    }
}
