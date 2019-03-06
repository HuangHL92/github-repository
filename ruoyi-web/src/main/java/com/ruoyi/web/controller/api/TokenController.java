package com.ruoyi.web.controller.api;

import cn.hutool.core.lang.UUID;
import com.ruoyi.common.annotation.ValidateRequest;
import com.ruoyi.common.base.ApiResult;
import com.ruoyi.common.enums.ResponseCode;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.demo.domain.Demo;
import com.ruoyi.framework.web.base.ApiBaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description $功能描述$
 * @Author yufei
 * @Date 2019-03-05 21:21
 **/
@Api("基础服务接口")
@RestController
@RequestMapping("/api/token/*")
public class TokenController extends ApiBaseController {

    @ApiOperation("获取令牌")
    @PostMapping("get")
    @CrossOrigin
    @ValidateRequest
    public ApiResult get( String account, String password)
    {

        //1.参数验证
        if(StringUtils.isEmpty(account) || StringUtils.isEmpty(password)) {
            return failed(ResponseCode.ILLEGAL_REQUEST);
        }
        //TODO 2.生成令牌写入redis

        //3.生成六位随机数
        String vcode = UUID.randomUUID().toString();

        //TODO 4.返回令牌
        return success(vcode);
    }

    @ApiOperation("刷新令牌")
    @PostMapping("refresh")
    @CrossOrigin
    public ApiResult refresh(String account, String password)
    {

        //1.参数验证
        if(StringUtils.isEmpty(account) || StringUtils.isEmpty(password)) {

            return failed(ResponseCode.ILLEGAL_REQUEST);
        }
        //TODO 2.生成令牌写入redis

        //3.生成六位随机数
        String vcode = UUID.randomUUID().toString();
        Demo demo = new Demo();
        demo.setName(vcode);

        //TODO 4.返回令牌
        return success(demo);
    }

}
