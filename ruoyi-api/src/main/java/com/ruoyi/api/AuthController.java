package com.ruoyi.api;

import com.ruoyi.base.ApiBaseController;
import com.ruoyi.common.base.ApiResult;
import com.ruoyi.common.enums.ResponseCode;
import com.ruoyi.common.utils.JedisUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.jwt.domain.Account;
import com.ruoyi.framework.jwt.service.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @Description $功能描述$
 * @Author yufei
 * @Date 2019-03-05 21:21
 **/
@Api(value = "/auth", description = "授权服务接口（基于JWT）")
@RestController
@RequestMapping("/api/auth/*")
public class AuthController extends ApiBaseController {


    @Autowired
    private TokenService tokenService;

    @Value("${api.token.expires}")
    private int  expires;

    private String TOKEN_KEY= "tokenCache:%s";

    @ApiOperation("用户验证（成功：返回token）")
    @PostMapping("getToken")
    public ApiResult getToken(@RequestParam(name="account") String account,
                         @RequestParam(name="password") String password)
    {

        //1.参数验证
        if(StringUtils.isEmpty(account) || StringUtils.isEmpty(password)) {
            return ApiResult.error(ResponseCode.ILLEGAL_REQUEST);
        }

        //2.验证用户是否合法
        if(!checkAccount(account,password)) {
            return ApiResult.error(ResponseCode.ERROR_LOGIN);
        }

        //3. 生成令牌写入redis
        //多少分钟后过期
        int etime = expires*60*1000;
        //过期时间
        long etime1=(System.currentTimeMillis()+etime)/1000L;
        String token = createToken(account,password,etime);

        HashMap map =new HashMap();
        map.put("expires", etime1);
        map.put("token",token);

        //4.返回令牌
        return ApiResult.success(map);
    }

    @ApiOperation("刷新令牌")
    @PostMapping("refreshToken")
    public ApiResult refreshToken(@RequestParam(name="account") String account,
                                  @RequestParam(name="password") String password)
    {

        //1.参数验证
        if(StringUtils.isEmpty(account) || StringUtils.isEmpty(password)) {

            return ApiResult.error(ResponseCode.ILLEGAL_REQUEST);
        }

        //2.验证用户的合法性
        if(!checkAccount(account,password)) {
            return ApiResult.error(ResponseCode.ERROR_LOGIN);
        }

        //3.生成令牌写入redis
        String token  =JedisUtils.get(String.format(TOKEN_KEY,account));
        //多少分钟后过期
        int etime = expires*60*1000;
        //过期时间
        long etime1=(System.currentTimeMillis()+etime)/1000L;
        if(token==null) {
            token = createToken(account,password,etime);
        }

        HashMap map =new HashMap();
        map.put("expires",etime1);
        map.put("token",token);

        //4.返回令牌
        return ApiResult.success(map);
    }


    /**
     * 创建token
     * @param account
     * @param password
     * @return
     */
    private String createToken(String account, String password,int exptime) {
        Account ac = new Account();
        ac.setId(account);
        ac.setUsername(account);
        ac.setPassword(password);
        String token = tokenService.getToken(ac);
        JedisUtils.set(String.format(TOKEN_KEY,account), token,exptime);
        return token;
    }

    /**
     * 验证账号(此处可以根据业务需要，改为其他表，比如会员表等）
     * @param account
     * @param password
     * @return
     */
    private boolean checkAccount(String account, String password) {
//        SysUser user = null;
//        try
//        {
//            user = loginService.login(account, password);
//        }
//        catch (CaptchaException e)
//        {
//            throw new AuthenticationException(e.getMessage(), e);
//        }
//        if(user==null) {
//            return false;
//        }

        return true;
    }
}
