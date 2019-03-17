package com.ruoyi.api;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.extra.mail.MailUtil;
import com.ruoyi.common.annotation.ValidateRequest;
import com.ruoyi.common.base.ApiResult;
import com.ruoyi.common.enums.ResponseCode;
import com.ruoyi.common.exception.user.CaptchaException;
import com.ruoyi.common.utils.JedisUtils;
import com.ruoyi.common.utils.RegexUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.jwt.domain.Account;
import com.ruoyi.framework.jwt.service.TokenService;
import com.ruoyi.framework.shiro.service.SysLoginService;
import com.ruoyi.framework.web.base.ApiBaseController;
import com.ruoyi.system.domain.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @Description $功能描述$
 * @Author yufei
 * @Date 2019-03-017 21:21
 **/
@Api(value = "/comm", description = "基础服务接口")
@RestController
@RequestMapping("/api/comm/*")
public class CommController extends ApiBaseController {


    @Autowired
    private TokenService tokenService;
    @Autowired
    private SysLoginService loginService;

    private String VCODE_KEY= "vcodeCache:%s";

    @ApiOperation("手机获得验证码（此时临时模拟直接返回，实际通过短信发送）")
    @PostMapping("vcodeSms")
    @ValidateRequest
    public ApiResult vcodeSms(@RequestParam(name="mobile") String mobile)
    {

        //1.参数验证
        if(StringUtils.isEmpty(mobile)) {
            return ApiResult.error(ResponseCode.ILLEGAL_REQUEST);
        }

        //2.证验是否未有为有效的手机号
        if(!RegexUtils.isMobile(mobile)){
            return ApiResult.error("不是有效的手机号码！");
        }

        //3. 验证码信息写入redis（验证码有效期5分钟）
        String vcode = RandomUtil.randomNumbers(6);
        JedisUtils.set(String.format(VCODE_KEY,mobile), vcode,300);

        //4.发送短信至手机 TODO 此时临时模拟直接返回，实际通过短信发送
        HashMap map =new HashMap();
        map.put("code",vcode);

        return ApiResult.success(map);
    }


    @ApiOperation("邮箱获得验证码")
    @PostMapping("vcodeMail")
    @ValidateRequest
    public ApiResult vcodeMail(@RequestParam(name="mail") String mail)
    {

        //1.参数验证
        if(StringUtils.isEmpty(mail)) {
            return ApiResult.error(ResponseCode.ILLEGAL_REQUEST);
        }

        //2.证验是否未有为有效的手机号
        if(!RegexUtils.isEmail(mail)){
            return ApiResult.error("不是有效的邮箱地址！");
        }

        //3. 验证码信息写入redis（验证码有效期5分钟）
        String vcode = RandomUtil.randomNumbers(6);
        JedisUtils.set(String.format(VCODE_KEY,mail), vcode,300);

        //4.发送短信至手机 TODO 此时临时模拟直接返回，实际通过短信发送
        MailUtil.send(mail,"吉运Java开发平台验证码","您的验证码为：" + vcode + "（5分钟内有效）",true);
        return ApiResult.success();
    }


    @ApiOperation("验证校验(手机)")
    @PostMapping("vcodeCheck")
    @ValidateRequest
    public ApiResult vcodeCheck(@RequestParam(name="mobile") String mobile, @RequestParam(name="code") String code)
    {

        //1.参数验证
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(code)) {
            return ApiResult.error(ResponseCode.ILLEGAL_REQUEST);
        }

        //2.证验是否未有为有效的手机号
        if(!RegexUtils.isMobile(mobile)){
            return ApiResult.error("不是有效的手机号码！");
        }

        //3. 从redis取的原始验证码
        String ocode = JedisUtils.get(String.format(VCODE_KEY,mobile));
        if(StringUtils.isEmpty(ocode)) {
            return ApiResult.error("验证码不存在或已失效！");
        }
        if(!ocode.equals(code)){
            return ApiResult.error("验证码错误！");
        }
        return ApiResult.success();
    }
}
