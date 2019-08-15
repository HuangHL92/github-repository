package com.ruoyi.framework.aspectj;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.ruoyi.common.annotation.ValidateRequest;
import com.ruoyi.common.enums.ResponseCode;
import com.ruoyi.common.exception.ApiRuntimeException;
import com.ruoyi.common.utils.JedisUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.utils.AESUtil;
import com.ruoyi.utils.CacheUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @Description RestfulApi 接口验证切面
 * @Author yufei
 * @Date 2019-03-08 10:45
 **/

@Aspect
@Component
public class AuthenticationAspect {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationAspect.class);

    @Value("${api.appsecret}")
    private String appsecret;

    @Value("${api.accessLimit.seconds}")
    private int accessLimitSeconds;

    @Value("${api.accessLimit.maxcount}")
    private int accessLimitMaxcount;

    @Value("${api.accessLimit.whiteip}")
    private String accessLimitWhiteIp;

    @Autowired
    private ISysUserService userService;

    // 配置织入点
    @Pointcut("within(com.ruoyi.api..*)")
    public void apiPointCut()
    {

    }


    /**
     * 前置通知 用于拦截操作
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "apiPointCut()")
    public void doBefore(JoinPoint joinPoint) throws Exception {
        handleValidate(joinPoint, null);
    }


    /**
     * 拦截异常操作
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(value = "apiPointCut()", throwing = "e")
    public void doAfter(JoinPoint joinPoint, Exception e) throws Exception {
        handleValidate(joinPoint, e);
    }




    /**
     * 请求验证处理
     * @param joinPoint
     * @param e
     */
    protected void handleValidate(final JoinPoint joinPoint, final Exception e) throws Exception {

        try
        {
            HttpServletRequest request = ServletUtils.getRequest();

            //1.重复请求过滤（ip+url）
            if(!accessLimitCheck(request)) {
                throw new ApiRuntimeException(ResponseCode.RE_REQUEST);
            }

            // 获得注解
            ValidateRequest annotation = getAnnotationLog(joinPoint);
            // 判断是否需要验证
            if (annotation == null || !annotation.required())
            {
                return;
            }

            //2.验证token
            //从 http 请求头中取出 token
            String token = CacheUtil.getToken4Request();

            if (StringUtils.isEmpty(token)) {
                //非法请求
                throw new ApiRuntimeException(ResponseCode.ILLEGAL_REQUEST);
            }

            try {
                //取得缓存中的token
                String cachekey = AESUtil.Decrypt(JWT.decode(token).getAudience().get(0),"");
                String account =cachekey.split("_")[0];
                String tokenvalue  = CacheUtil.getToken4Cache(account);
                if(StringUtils.isEmpty(tokenvalue) || !token.equals(tokenvalue)) {
                    throw new ApiRuntimeException(ResponseCode.ERROR_40014);
                }

                //根据account取得用户信息
                SysUser user = userService.selectUserByLoginName(account);
                if(user==null) {
                    throw new ApiRuntimeException(ResponseCode.ERROR_40014);
                }

                // 验证token
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
                jwtVerifier.verify(token);

            } catch (Exception j) {
                throw new ApiRuntimeException(ResponseCode.ERROR_40014);
            }

            //验证请求次数限制(0代表不限制)
//            int requestLimit = NumberUtil.parseInt(app.getRate());
//            if(requestLimit>0) {
//                if(!limitCheck(app.getAppid(),requestLimit)) {
//                    throw new ApiRuntimeException(ResponseCode.ERROR_45009);
//                }
//            }

        }
        catch (Exception exp)
        {
            // 记录本地异常日志
            log.error("接口请求异常:{}", exp.getMessage());
            throw exp;
        }
    }


    /**
     * 是否存在注解，如果存在就获取
     */
    private ValidateRequest getAnnotationLog(JoinPoint joinPoint) throws Exception
    {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null)
        {
            return method.getAnnotation(ValidateRequest.class);
        }
        return null;
    }


    /**
     * 是否拒绝服务 (5秒内>5次)
     * @return
     */
    private boolean accessLimitCheck(HttpServletRequest request){


        /*String ip = IpUtils.getIpAddr(request);
        String url = request.getRequestURL().toString();
        String tempkey = ( ip + "#" + url).replaceAll(":","");
        String[] ipList = accessLimitWhiteIp.split(",");
        for (String whiteip:ipList) {
            if(whiteip.equals(ip)){
                return true;
            }
        }
        long count= JedisUtils.setIncr(tempkey,accessLimitSeconds);
        if(count>accessLimitMaxcount){
            return false;
        }*/
        return true;
    }


    /**
     * 接口访问限制
     * @return
     */
    private boolean limitCheck(String appid, int limit){
        //保存某API请求次数到缓存（此缓存每天00:00定时清空，由TaskCache任务完成）
        long count= JedisUtils.setIncr(appid,60*60*24);
        if(count>limit){
            return false;
        }
        return true;
    }

}
