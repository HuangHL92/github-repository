package com.ruoyi.framework.aspectj;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.ruoyi.common.annotation.WriteLog2DB;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.http.HttpUtils;
//import com.ruoyi.monitor.domain.LogExchange;
//import com.ruoyi.utils.CacheUtil;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * 接口访问日志记录处理
 * 
 * @author ruoyi
 */
@Aspect
@Component
public class ApiLogAspect
{

    private static final Logger log = LoggerFactory.getLogger("restful_api");

    @Value("${logging.requestlog}")
    private boolean requestlog;

    // 开始时间
    ThreadLocal<Long> startTime = new ThreadLocal<>();

    //日志对象
    JSONObject logobj = new JSONObject();

    /***
     * 配置织入点
     */
    @Pointcut("within(com.ruoyi.api..*)")
    public void logPointCut()
    {
    }

    /**
     * 前置通知 用于拦截操作
     *
     * @param joinPoint 切点
     */
    @Before("logPointCut()")
    public void doBefore(JoinPoint joinPoint)
    {
        startTime.set(System.currentTimeMillis());

        handleLog(joinPoint, null);

    }

    /**
     * 执行后 用于拦截操作
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()",returning = "rvt")
    public void doAfter(JoinPoint joinPoint,Object rvt)
    {
        //控制是否输出详细请求信息日志
        if(requestlog){
            long end = System.currentTimeMillis();
            long total =end - startTime.get();
            startTime.remove();
            MethodSignature signature  = (MethodSignature)joinPoint.getSignature();
            Method method = signature.getMethod();
            log.info("{}.{}: 耗时：{}毫秒 返回参数：{}",method.getDeclaringClass().getName(),method.getName(),total,new Gson().toJson(rvt));

            // 获得注解
            WriteLog2DB annotation = getAnnotationLog(joinPoint);
            // 判断是否需要写日志
            if (annotation != null )
            {
                if(annotation.required()) {
                    //写入日志
                    logobj.put("respIpadr","");
                    logobj.put("rsltIndx","1");
                    logobj.put("rsltCnt","5");
                    logobj.put("cmplReqTime",DateTime.now());
                    logobj.put("resvInfo","");
                    logobj.put("jhptDelete","0");
                    logobj.put("jhptUpdateTime",DateTime.now());
//                    LogExchange log = Convert.convert(LogExchange.class,logobj);
//                    //先写入缓存，当达到一定数量之后一次写入数据库
//                    CacheUtil.write2Log(log);
                }

            }

        }

    }

    /**
     * 拦截异常操作
     * 
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfter(JoinPoint joinPoint, Exception e)
    {
        handleLog(joinPoint, e);

    }


    protected void handleLog(final JoinPoint joinPoint, final Exception e)
    {
        //控制是否输出详细请求信息日志
        if(!requestlog){
            return;
        }

        try
        {
            StringBuffer sb = new StringBuffer();

            HttpServletRequest request = ServletUtils.getRequest();
            String agentString = request.getHeader("User-Agent");
            UserAgent userAgent = UserAgent.parseUserAgentString(agentString);
            OperatingSystem operatingSystem = userAgent.getOperatingSystem(); // 操作系统信息
            eu.bitwalker.useragentutils.DeviceType deviceType = operatingSystem.getDeviceType(); // 设备类型
            String url = request.getRequestURL().toString();
            // 请求的地址
            String ip = HttpUtils.getClientIP(request);

            if(e!=null) {

                sb.append("\r\n").append("*******************************异常-S***************************").append("\r\n");
                sb.append("异常信息：").append(e.getMessage()).append("\r\n");
                sb.append("URL：" + url).append("\r\n");
                sb.append("IP：" + ip).append("\r\n");
                Enumeration<String> header =request.getHeaderNames();
                while (header.hasMoreElements()){
                    String s = header.nextElement();
                    sb.append(s+":"+request.getHeader(s)).append("\r\n");
                }
                sb.append("*******************************异常-E***************************");
                log.error(sb.toString());
            } else {
                sb.append("\r\n").append("*******************************请求信息-S***************************").append("\r\n");
                sb.append("URL：" + url).append("\r\n");
                sb.append("IP：" + ip).append("\r\n");
                sb.append("请求时间：" + DateTime.now().toString("\"yyyy年MM月dd日 hh时mm分ss秒\"")).append("\r\n");
                sb.append("操作系统：" + operatingSystem.getName()).append("\r\n");
                sb.append("设备类型：" + deviceType).append("\r\n");
                Enumeration<String> header = request.getHeaderNames();
                StringBuffer rsb = new StringBuffer();
                while (header.hasMoreElements()) {
                    String s = header.nextElement();
                    sb.append(s + ":" + request.getHeader(s)).append("\r\n");

                }

                sb.append("参数：" ).append("\r\n");
//                rsb.append("x-access-token:" + request.getHeader("x-access-token")).append(",");
                Map parms = getParameterMap(request);
                Object jsonObject=JSONObject.toJSON(parms);
                rsb.append(jsonObject.toString());
                sb.append(jsonObject.toString());

                sb.append("\r\n").append("*******************************请求信息-E***************************");
                log.info(sb.toString());

//                {
//                    "logId": "04e78551-a93e-e232-f37d-b9c29fe749d8",
//                        "dstrCd": "310106",
//                        "dstrNme": "静安区",
//                        "logTypeIndx": "1",
//                        "reqNme": "系统1",
//                        "reqIpadr": "192.562.4.28",
//                        "userNme": "admin",
//                        "respNme": "响应系统1",
//                        "respIpadr": "18.5.13.237",
//                        "reqData": "webService1(param1, param2)",
//                        "reqDataExt": "rtn1, rtn2, rtn3, rtn4, rtn5",
//                        "rsltIndx": "1",
//                        "rsltCnt": "5",
//                        "strtReqTime": "2019/5/17 14:33:21",
//                        "cmplReqTime": "2019/5/17 14:33:23",
//                        "resvInfo": "XQ00300SP5023900000046",
//                        "jhptDelete": "0",
//                        "jhptUpdateTime": "2019/6/19 10:33:23"
//                }

                // 创建提交给市大数据中心的日志
                logobj.put("dstrCd", "310116");
                logobj.put("dstrNme", "金山区");
                logobj.put("logTypeIndx", "1");
                logobj.put("reqNme", "");
                logobj.put("reqIpadr", ip);
                logobj.put("reqData", url);
                logobj.put("reqDataExt", rsb);
                logobj.put("userNme", "");
                logobj.put("respNme", "");
                logobj.put("strtReqTime", DateTime.now());

            }


        }
        catch (Exception exp)
        {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }


    /**
     * 是否存在注解，如果存在就获取
     */
    private WriteLog2DB getAnnotationLog(JoinPoint joinPoint)
    {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null)
        {
            return method.getAnnotation(WriteLog2DB.class);
        }
        return null;
    }


    private static Map<String, Object> getParameterMap(HttpServletRequest request) {
        Map<String, String[]> properties = request.getParameterMap();//把请求参数封装到Map<String, String[]>中
        Map<String, Object> returnMap = new HashMap<String, Object>();
        Iterator<Map.Entry<String, String[]>> iter = properties.entrySet().iterator();
         String name = "";
         String value = "";
         while (iter.hasNext()) {
                 Map.Entry<String, String[]> entry = iter.next();
                name = entry.getKey();
                 Object valueObj = entry.getValue();
                 if (null == valueObj) {
                         value = "";
                     } else if (valueObj instanceof String[]) {
                         String[] values = (String[]) valueObj;
                         for (int i = 0; i < values.length; i++) { //用于请求参数中有多个相同名称
                                value = values[i] + ",";
                            }
                         value = value.substring(0, value.length() - 1);
                     } else {
                         value = valueObj.toString();//用于请求参数中请求参数名唯一
                 }
                 returnMap.put(name, value);
             }
        return returnMap;
    }

}
