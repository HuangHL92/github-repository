//package com.ruoyi.framework.aspectj;
//
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.crypto.SecureUtil;
//import com.ruoyi.common.annotation.ValidateRequest;
//import com.ruoyi.common.enums.ResponseCode;
//import com.ruoyi.common.exception.ApiRuntimeException;
//import com.ruoyi.common.utils.JedisUtils;
//import com.ruoyi.common.utils.ServletUtils;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.Signature;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//import java.lang.reflect.Method;
//import java.util.Arrays;
//
///**
// * @Description restful接口请求过滤
// * @Author yufei
// * @Date 2019-03-06 08:06
// **/
//
//@Aspect
//@Component
//public class ApiValidateAspect {
//
//    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);
//
//
//    private int performanceBadValue;
//
//    @Value("${api.overtime}")
//    int apiOverTime=15;
//
//    @Value("${api.nonceovertime}")
//    int nonceOverTime=15;
//
//    // 配置织入点
//    @Pointcut("@annotation(com.ruoyi.common.annotation.ValidateRequest)")
//    public void apiPointCut()
//    {
//
//    }
//
//    /**
//     * 前置通知 用于拦截操作
//     *
//     * @param joinPoint 切点
//     */
//    @AfterReturning(pointcut = "apiPointCut()")
//    public void doBefore(JoinPoint joinPoint) throws Exception {
//        handleValidate(joinPoint, null);
//    }
//
//    /**
//     * 拦截异常操作
//     *
//     * @param joinPoint
//     * @param e
//     */
//    @AfterThrowing(value = "apiPointCut()", throwing = "e")
//    public void doAfter(JoinPoint joinPoint, Exception e) throws Exception {
//        handleValidate(joinPoint, e);
//    }
//
//
//
//
//    /**
//     * 请求验证处理
//     * @param joinPoint
//     * @param e
//     */
//    protected void handleValidate(final JoinPoint joinPoint, final Exception e) throws Exception {
//        try
//        {
//            // 获得注解
//            ValidateRequest annotation = getAnnotationLog(joinPoint);
//            // 判断是否需要验证
//            if (annotation == null || !annotation.required())
//            {
//                return;
//            }
//
//            // 2 获取参数及参数验证
//            // 2.1 appKey
//            String appKey = "";
//            // 2.2 timestamp
//            String timestamp = "";
//            // 2.3 nonce
//            String nonce = "";
//            // 2.4 signature
//            String signature = "";
//
//
//
//            HttpServletRequest request = ServletUtils.getRequest();
//
//            //TODO 请求头部中参数进行解密
//
//            //验证请求参数(验证头部、验证请求参数)
//            if("request".equals(annotation.vtype())) {
//                // 2.1 appKey
//                appKey = request.getParameter("appKey");
//                // 2.2 timestamp
//                timestamp = request.getParameter("timestamp");
//                // 2.3 nonce
//                nonce = request.getParameter("nonce");
//                // 2.4 signature
//                signature = request.getParameter("signature");
//            } else {
//                //验证请求头部
//                if(request instanceof HttpServletRequest) {
//
//                    String token = request.getHeader("token");
//                    // 2.1 appKey
//                    appKey = request.getHeader("appKey");
//                    // 2.2 timestamp
//                    timestamp = request.getHeader("timestamp");
//                    // 2.3 nonce
//                    nonce = request.getHeader("nonce");
//                    // 2.4 signature
//                    signature = request.getHeader("signature");
//                }
//
//            }
//
//
//            // 2.5 参数验证
//            if (StrUtil.hasBlank(appKey) || StrUtil.hasBlank(timestamp)
//                    || StrUtil.hasBlank(nonce) || StrUtil.hasBlank(signature)) {
//                throw new ApiRuntimeException(ResponseCode.ERROR_REQUEST);
//            }
//
//            // 3 验证时效性
//            long timestampL = Long.parseLong(timestamp);
//            long nowStamp = System.currentTimeMillis();
//            long timeBetween = nowStamp > timestampL ? nowStamp - timestampL : timestampL - nowStamp;
//            if (timeBetween > apiOverTime) {
//                throw new ApiRuntimeException(ResponseCode.OVER_TIME);
//            }
//
//            // 4 验证nonce是否存在
//            if (!StrUtil.hasBlank(JedisUtils.get(nonce))) {
//                throw new ApiRuntimeException(ResponseCode.OVER_TIME);
//            }
//
//            // 5 根据appKey获取appSecret TODO
//            String appSecret = "sd73hfksgaddk";
////        CasSystem app = casSystemService.findUniqueByProperty("app_key", appKey);
////        if (app == null) {
////            throw new ApiException(ApiErrorCode.KEY_NOT_FOUNT);
////        }
//
//            // 6 signature验证
//            if (!checkSignature(appKey, appSecret, timestamp, nonce, signature)) {
//                throw new ApiRuntimeException(ResponseCode.ILLEGAL_REQUEST);
//            }
//
//            // 7 记录nonce，设置超时时间 15分钟
//            JedisUtils.set(nonce, nonce, nonceOverTime);
//
//
//        }
//        catch (Exception exp)
//        {
//            // 记录本地异常日志
//            log.error("接口请求异常:{}", exp.getMessage());
//            throw exp;
//        }
//    }
//
//
//    /**
//     * 是否存在注解，如果存在就获取
//     */
//    private ValidateRequest getAnnotationLog(JoinPoint joinPoint) throws Exception
//    {
//        Signature signature = joinPoint.getSignature();
//        MethodSignature methodSignature = (MethodSignature) signature;
//        Method method = methodSignature.getMethod();
//
//        if (method != null)
//        {
//            return method.getAnnotation(ValidateRequest.class);
//        }
//        return null;
//    }
//
//
//    /**
//     * signature验证
//     *
//     * @param appKey
//     * @param timestamp
//     * @param nonce
//     * @param signature
//     * @return
//     */
//    private boolean checkSignature (String appKey, String appSecret, String timestamp, String nonce, String signature) {
//        // 1 将参数放进数组
//        String[] arrTmp = { appKey, timestamp, nonce };
//        // 2 对数组进行排序
//        Arrays.sort(arrTmp);
//        // 3 将数组进行拼接
//        StringBuilder sb = new StringBuilder();
//        for (String temp : arrTmp) {
//            sb.append(temp);
//        }
//        // 4 验证
//        String tmpSignature = SecureUtil.md5(sb.toString() + appSecret);
//        return tmpSignature.equals(signature);
//    }
//
//}
