package com.ruoyi.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import com.auth0.jwt.JWT;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.ruoyi.common.utils.JedisUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.jwt.domain.Account;
import com.ruoyi.framework.jwt.service.TokenService;
//import com.ruoyi.monitor.domain.JobLog;
//import com.ruoyi.monitor.domain.LogExchange;
//import com.ruoyi.monitor.service.IJobLogService;
//import com.ruoyi.monitor.service.ILogExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static com.ruoyi.common.base.ApiConst.TOKEN_KEY;


@Component
public class CacheUtil {


    private static TokenService tokenService;

//    private static IJobLogService logService;
//
//    private static ILogExchangeService logExchangeService;



    @Autowired
    TokenService tokenService2;

//    @Autowired
//    IJobLogService logService2;
//
//    @Autowired
//    ILogExchangeService logExchangeService2;


    private static int cachenum;
    @Value("${logging.cachenum}")
    public void setCachenum(int num) {
        cachenum = num;
    }

    @PostConstruct
    public void beforeInit() {
        tokenService = tokenService2;
//        logService = logService2;
//        logExchangeService = logExchangeService2;

    }

    /***
     * 生成tokent,写入缓存(未启用)
     * @param appid
     * @param secret
     * @param timeout
     * @return
     */
    public static String createToken(String appid, String secret, int timeout) {
        Account ac = new Account();
        ac.setId(appid);
        ac.setUsername(appid);
        ac.setPassword(secret);
        String token = tokenService.getToken(ac);
        JedisUtils.set(String.format(TOKEN_KEY,appid), token,timeout*60);
        return token;
    }

    /***
     * 取得token
     * @param key
     * @return
     */
    public static  String getToken4Cache(String key) {
        return JedisUtils.get(String.format(TOKEN_KEY,key));
    }

    /***
     * 刷新token
     * @param key
     * @return
     */
    public static  void refreshToken4Cache(String key,String token,int timeout) {
         JedisUtils.setObject(String.format(TOKEN_KEY,key),token,timeout);
    }

    /***
     * 取得token
     * @return
     */
    public static  String getToken4Request() {
        HttpServletRequest request = ServletUtils.getRequest();
        return request.getHeader("x-access-token");
    }

    /***
     * 取得APPID
     * @return
     */
    public static  String getAppID(String token) {
        return AESUtil.Decrypt(JWT.decode(token).getAudience().get(0),"").split("_")[0];
    }



//    /***
//     * 取得应用
//     * @param log
//     * @return
//     */
//    public static void writeLog(JobLog log) {
//
//        if(log==null || StringUtils.isEmpty(log.getDescription())) {
//            return;
//        }
//
//        List<Object> cacheList  = (List<Object>)JedisUtils.getObject(LOG_KEY);
//        cacheList =cacheList==null? new ArrayList<>():cacheList;
//
//        //先写入cacheList，再判断是否达到写入数据库个数，未达到写入缓存
//        cacheList.add(Convert.convert(Object.class,log));
//        if(cacheList.size()<cachenum) {
//            JedisUtils.setObject(LOG_KEY,cacheList,0);
//        } else {
//            //批量写入并清空缓存
//            try {
//                //批量写入
//                logService.saveBatch((Collection<JobLog>) Convert.toList(JobLog.class,cacheList));
//            } catch (Exception e1) {
//                e1.printStackTrace();
//            }
//            //清空缓存
//            JedisUtils.delObject(LOG_KEY);
//        }
//
//    }
//
//    /***
//     * 写入日志（市级）：先写入缓存，当达到一定数量之后一次写入数据库
//     * @param log
//     * @return
//     */
//    public static void write2Log(LogExchange log) {
//
//        //1.请求内容为空，不予保存
//        if(log==null || StringUtils.isEmpty(log.getReqData())) {
//            return;
//        }
//
//        List<Object> cacheList  = (List<Object>)JedisUtils.getObject(LOG_KEY2);
//        cacheList =cacheList==null? new ArrayList<>():cacheList;
//        //2.设置log的id值(UUID确保数据id唯一)
//        log.setLogId(IdUtil.randomUUID());
//        //交换平台时间：当前时间
//        log.setJhptUpdateTime(new Date());
//        //3.先写入cacheList，再判断是否达到写入数据库个数，未达到写入缓存
//        cacheList.add(Convert.convert(Object.class,log));
//        if(cacheList.size()<cachenum) {
//            JedisUtils.setObject(LOG_KEY2,cacheList,0);
//        } else {
//            //4.批量写入并清空缓存
//            try {
//                //4.1转换写入的数据库
//                DynamicDataSourceContextHolder.push("front_dsjzx");
//                //4.2防止插入数据报错，不清空缓存
//                try {
//                    //批量写入
//                    logExchangeService.saveBatch((Collection<LogExchange>) Convert.toList(LogExchange.class,cacheList));
//                } catch (Exception e1) {
//                    e1.printStackTrace();
//                }
//                //4.3清空缓存
//                JedisUtils.delObject(LOG_KEY2);
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                DynamicDataSourceContextHolder.clear();
//            }
//        }
//
//    }

}
