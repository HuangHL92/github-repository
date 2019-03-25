package com.ruoyi.api;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.area.demo.domain.Demo;
import com.ruoyi.area.demo.service.IDemoService;
import com.ruoyi.common.annotation.ValidateRequest;
import com.ruoyi.common.base.ApiResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.base.ApiBaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Api(value = "/test", description = "测试接口")
@RestController
@RequestMapping("/api/test/*")
public class TestController extends ApiBaseController {


    @Autowired
    private  IDemoService  demoService;

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

    @ApiOperation("异常模拟")
    @GetMapping("getException")
    public ApiResult get_exception()
    {

        int i=1/0;

        return ApiResult.success();
    }



    @ApiOperation("mybatisPlus 翻页获得数据Demo1")
    @GetMapping("/mptest4page1/{page}/{size}")
    public Map<String, Object> mptest4page1(@PathVariable Integer page, @PathVariable Integer size) {
        Map<String, Object> map = new HashMap<>();
        Page<Demo> questionStudent = demoService.selectList4Page1(new Page<>(page, size));
        if (questionStudent.getRecords().size() == 0) {
            map.put("code", 400);
        } else {
            map.put("code", 200);
            map.put("data", questionStudent);
        }
        return map;
    }

    @ApiOperation("mybatisPlus 多表翻页获得数据Demo2")
    @GetMapping("/mptest4page2/{page}/{size}/{keywords}")
    public Map<String, Object> mptest4page2(@PathVariable Integer page, @PathVariable Integer size,@RequestParam(name="keywords",required = false) String keywords) {
        Map<String, Object> map = new HashMap<>();
        Demo demo = new Demo();
        demo.setName(keywords);
        Page<Demo> list = demoService.selectList4Page2(new Page<>(page, size),demo);

        if (list.getRecords().size() == 0) {
            map.put("code", 400);
        } else {
            map.put("code", 200);
            map.put("data", list);
        }
        return map;
    }
}
