package com.ruoyi.api;

import cn.hutool.core.lang.UUID;
import com.ruoyi.common.base.ApiResult;
import com.ruoyi.common.enums.ResponseCode;
import com.ruoyi.common.utils.JedisUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.jwt.domain.Account;
import com.ruoyi.framework.jwt.service.TokenService;
import com.ruoyi.framework.web.base.ApiBaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @Description $功能描述$
 * @Author yufei
 * @Date 2019-03-05 21:21
 **/
@Api("基础服务接口")
@RestController
@RequestMapping("/api/token/*")
public class TokenController extends ApiBaseController {


    @Autowired
    private TokenService tokenService;

    private String TOKEN_KEY= "tokenCache:%s";

    @ApiOperation("获取令牌")
    @PostMapping("get")
    public ApiResult get( String account, String password)
    {

        //1.参数验证
        if(StringUtils.isEmpty(account) || StringUtils.isEmpty(password)) {
            return ApiResult.error(ResponseCode.ILLEGAL_REQUEST);
        }

        //TODO 2.验证用户是否合法

        //3. 生成令牌写入redis
        String token = createToken(account,password);

        HashMap map =new HashMap();
        map.put("expires",3600);
        map.put("token",token);

        //TODO 4.返回令牌
        return ApiResult.success(map);
    }

    @ApiOperation("刷新令牌")
    @PostMapping("refresh")
    public ApiResult refresh(String account, String password)
    {

        //1.参数验证
        if(StringUtils.isEmpty(account) || StringUtils.isEmpty(password)) {

            return ApiResult.error(ResponseCode.ILLEGAL_REQUEST);
        }

        //TODO 2.生成令牌写入redis
        String token  =JedisUtils.get(String.format(TOKEN_KEY,account));
        if(token==null) {
            token = createToken(account,password);
        }

        HashMap map =new HashMap();
        map.put("expires",3600);
        map.put("token",token);

        //TODO 4.返回令牌
        return ApiResult.success(map);
    }


    private String createToken(String account, String password) {
        Account ac = new Account();
        ac.setId(account);
        ac.setUsername(account);
        ac.setPassword(password);
        String token = tokenService.getToken(ac);
        JedisUtils.set(String.format(TOKEN_KEY,account), token,3600);
        return token;
    }
}
