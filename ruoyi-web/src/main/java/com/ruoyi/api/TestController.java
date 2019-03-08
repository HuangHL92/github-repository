package com.ruoyi.api;

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


@Api(value = "/test", description = "测试接口")
@RestController
@RequestMapping("/api/test/*")
public class TestController extends ApiBaseController {

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

    @ApiOperation("get测试(V2)")
    @GetMapping("v2/get")
    public ApiResult get_v2(String mobile)
    {
        //1.参数验证
        if(StringUtils.isEmpty(mobile)) {
            return ApiResult.error("参数错误");
        }

        return ApiResult.success("您输入的是(V2)：" + mobile);
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

    @ApiOperation(value = "请求验证测试",notes="请先调用/auth/login 接口取得token信息，并将token放入请求头部key为token")
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
