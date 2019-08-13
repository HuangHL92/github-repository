package com.ruoyi.common.utils.sms;


import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.ruoyi.common.utils.JedisUtils;
import com.ruoyi.common.utils.Md5Utils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

/**
 * 短信工具类
 */
public class SmsUtils {


    private static Logger logger = LoggerFactory.getLogger(SmsUtils.class);


    /**
     * 发送
     * @param mobile
     * @param msg
     * @return 成功：success 错误：错误消息
     */
    public  String send(String mobile, String msg) {

        try {
            //默认没有指定通道情况下，使用第一个通道做默认发送通道
            sendMsg(mobile, msg, "1");
        } catch (Exception ex) {
            return ex.getMessage();
        }
        return "success";
    }

    /**
     * 发送
     * @param mobile
     * @param msg
     * @param channel
     * @return 成功：success 错误：错误消息
     */
    public String send(String mobile, String msg, String channel) {

        try {
            sendMsg(mobile, msg, channel);
        } catch (Exception ex) {
            return ex.getMessage();
        }
        return "success";
    }


    /**
     * 发送(批量发送)
     * @param mobiles
     * @param msg
     * @return
     */
    public static String send(List<String> mobiles, String msg) {
        return "";
    }


    /**
     * 获得通道配置
     * @param channel
     * @return
     */
    private static String sendMsg(String mobile, String msg, String channel) throws Exception {


        SmsProperties.SmsConfig conf;
        List<SmsProperties.SmsConfig> configs = SmsConfiguration.getProperties().getConfigs();
        for (SmsProperties.SmsConfig config:configs) {
            if(channel.equals(config.getChannel())) {
                conf = config;
            }
        }
        conf = configs.get(0);

        if(conf ==null || StringUtils.isEmpty(conf.getAccount())) {
            throw new  Exception("短信通道配置错误，请检查配置文件。");
        }

        String res = "";
        switch (channel) {
            case "1":
                res = sendMsg_1(conf,mobile,msg);
                break;
            case "2":
                res = sendMsg_2(conf,mobile,msg);
                break;
            case "3":
                res = sendMsg_3(conf,mobile,msg);
                break;
        }

        return res;

    }

    /***
     * 发送通道1
     * @param config
     * @param mobile
     * @param msg
     * @return
     */
    private static String sendMsg_1(SmsProperties.SmsConfig config,String mobile, String msg) throws IOException {

        String res="";

        StringBuffer sb = new StringBuffer();
        sb.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.nineorange.com\">");
        sb.append("<soapenv:Header/>");
        sb.append("<soapenv:Body>");
        sb.append("<ser:sendMessage>");
        sb.append("<ser:account>" + config.getAccount() + "</ser:account>");
        sb.append("<ser:password>" + config.getPassword() + "</ser:password>");
        sb.append("<ser:destmobile>" + mobile + "</ser:destmobile>");
        sb.append("<ser:msgText>【" + config.getSign() + "】" + msg + "</ser:msgText>");
        sb.append("</ser:sendMessage>");
        sb.append("</soapenv:Body>");
        sb.append("</soapenv:Envelope>");

        HashMap<String, String> headers = new HashMap<>();
        headers.put("headers", "text/xml");
        headers.put("SOAPAction", "application/soap+xml; charset=utf-8");

        res = HttpRequest.post(config.getUrl())
                .addHeaders(headers)
                .body(sb.toString())
                .timeout(20000)//超时，毫秒
                .execute().body();

       logger.debug(config.getRemark() + "发送短信结果：" + res);

       return res;

    }

    /***
     * 发送通道2
     * @param config
     * @param mobile
     * @param msg
     * @return
     */
    private static String sendMsg_2(SmsProperties.SmsConfig config,String mobile, String msg) throws IOException {
         /*
        功能: 企信通 HTTP接口 发送短信
        修改日期:	2017-03-19
        说明: http://api.cnsms.cn/?ac=send&uid=用户账号&pwd=MD5位32密码&mobile=号码&content=内容
        状态:
            100 发送成功
            101 验证失败
            102 短信不足
            103 操作失败
            104 非法字符
            105 内容过多
            106 号码过多
            107 频率过快
            108 号码内容空
            109 账号冻结
            110 禁止频繁单条发送
            111 系统暂定发送
            112 号码不正确
            120 系统升级
        */

        // 创建StringBuffer对象用来操作字符串
        StringBuffer sb = new StringBuffer(config.getUrl());
        // 向StringBuffer追加用户名
        sb.append("ac=send&uid="+config.getAccount());//在此申请企信通uid，并进行配置用户名
        // 向StringBuffer追加密码（密码采用MD5 32位 小写）
        sb.append("&encode=utf8");
        // 向StringBuffer追加密码（密码采用MD5 32位 小写）
        sb.append("&pwd="+ SecureUtil.md5(config.getPassword()));//在此申请企信通uid，并进行配置密码
        // 向StringBuffer追加手机号码
        sb.append("&mobile="+mobile);
        // 向StringBuffer追加消息内容转URL标准码
        sb.append("&content="+ URLEncoder.encode(msg,"utf8"));
        // 创建url对象
        URL url = new URL(sb.toString());
        // 打开url连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 设置url请求方式 ‘get’ 或者 ‘post’
        connection.setRequestMethod("POST");
        // 发送
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        // 返回发送结果
        String res = in.readLine();

        logger.debug(config.getRemark() + "发送短信结果：" + res);

        return res;
    }


    /***
     * 发送通道3
     * @param config
     * @param mobile
     * @param msg
     * @return
     */
    private static String sendMsg_3(SmsProperties.SmsConfig config,String mobile, String msg) {
        return "";
    }
}
