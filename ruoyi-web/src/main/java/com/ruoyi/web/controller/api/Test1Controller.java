package com.ruoyi.web.controller.api;

import cn.hutool.core.util.RandomUtil;
import com.ruoyi.common.annotation.ValidateRequest;
import com.ruoyi.common.base.ApiResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.base.ApiBaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("demo")
@RestController
@RequestMapping("/api/test/*")
public class Test1Controller extends ApiBaseController {

    @ApiOperation("get测试")
    @GetMapping("get")
    public ApiResult get(String mobile)
    {
        //1.参数验证
        if(StringUtils.isEmpty(mobile)) {
            return ApiResult.error("参数错误");
        }

        return ApiResult.success("您输入的是：" + mobile);
    }

    @ApiOperation("post测试")
    @PostMapping("post")
    public ApiResult post(String mobile)
    {
        //1.参数验证
        if(StringUtils.isEmpty(mobile) ) {
            return ApiResult.error("参数错误");
        }

        return ApiResult.success("您输入的是：" + mobile);
    }

    @ApiOperation("请求验证测试")
    @GetMapping("validateRequest")
    @ValidateRequest
    public ApiResult validateRequest(String mobile)
    {
        //1.参数验证
        if(StringUtils.isEmpty(mobile)) {
            return ApiResult.error("参数错误");
        }

        return ApiResult.success("您输入的是：" + mobile);
    }
}
