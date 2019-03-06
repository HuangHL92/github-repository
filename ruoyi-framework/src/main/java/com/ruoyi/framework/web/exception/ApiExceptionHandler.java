//package com.ruoyi.framework.web.exception;
//
//import com.ruoyi.common.base.ApiResult;
//import com.ruoyi.common.base.ApiResult;
//import com.ruoyi.common.exception.BusinessException;
//import com.ruoyi.framework.util.PermissionUtils;
//import org.apache.shiro.authz.AuthorizationException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.HttpRequestMethodNotSupportedException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
///**
// * @Description $功能描述$
// * @Author yufei
// * @Date 2019-03-06 09:53
// **/
//
//@RestControllerAdvice
//public class ApiExceptionHandler {
//
//    private static final Logger log = LoggerFactory.getLogger(DefaultExceptionHandler.class);
//
//    /**
//     * 权限校验失败
//     */
//    @ExceptionHandler(AuthorizationException.class)
//    public ApiResult handleAuthorizationException(AuthorizationException e)
//    {
//        log.error(e.getMessage(), e);
//        return ApiResult.failed(PermissionUtils.getMsg(e.getMessage()));
//    }
//
//    /**
//     * 请求方式不支持
//     */
//    @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
//    public ApiResult handleException(HttpRequestMethodNotSupportedException e)
//    {
//        log.error(e.getMessage(), e);
//        return ApiResult.failed("不支持' " + e.getMethod() + "'请求");
//    }
//
//    /**
//     * 拦截未知的运行时异常
//     */
//    @ExceptionHandler(RuntimeException.class)
//    public ApiResult notFount(RuntimeException e)
//    {
//        log.error("运行时异常:", e);
//        return ApiResult.failed("运行时异常:" + e.getMessage());
//    }
//
//    /**
//     * 系统异常
//     */
//    @ExceptionHandler(Exception.class)
//    public ApiResult handleException(Exception e)
//    {
//        log.error(e.getMessage(), e);
//        return ApiResult.failed("服务器错误，请联系管理员");
//    }
//
//    /**
//     * 业务异常
//     */
//    @ExceptionHandler(BusinessException.class)
//    public ApiResult businessException(BusinessException e)
//    {
//        log.error(e.getMessage(), e);
//        return ApiResult.failed(e.getMessage());
//    }
//
//}
