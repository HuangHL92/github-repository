package com.ruoyi.common.enums;

/**
 * @Description restful接口返回code
 * @Author yufei
 * @Date 2019-03-05 21:56
 **/
public enum ResponseCode {

    FAILED(-1L, "操作失败！请联系管理员！"),
    SUCCESS(200L, "成功"),
    ERROR_REQUEST(4001, "请求参数错误"),
    OVER_TIME(4002, "请求参数过期"),
    KEY_NOT_FOUNT(4003, "AppKey不存在"),
    RE_REQUEST(4004, "请求过于频繁"),
    ILLEGAL_REQUEST(4005, "非法请求"),
    ILLEGAL_ACCOUNT(4006, "账号信息不存在"),
    ERROR_LOGIN(4007, "账号或密码错误"),
    ILLEGAL_TOKEN(4008, "非法请求（token验证失败）")

    ,ERROR_400(400 ,"服务器不理解请求的语法")
    ,ERROR_401(401,"请求要求身份验证")
    ,ERROR_403(403,"服务器拒绝请求")
    ,ERROR_404(404 ,"服务器找不到请求的地址")
    ,ERROR_405(405,"禁用请求中指定的方法")
    ,ERROR_406 (406, "无法使用请求的内容特性响应请求")
    ,ERROR_407 (407,"需要代理授权")
    ,ERROR_408(408, "请求超时")
    ,ERROR_415(415, "不支持当前媒体类型")
    ,ERROR_500(500, "服务器内部错误")
    ,ERROR_501(501, "服务器不具备完成请求的功能")
    ,ERROR_502(502, "错误网关")
    ,ERROR_503(503, "服务不可用")
    ,ERROR_504(504, "网关超时")
    ,ERROR_40001(40001, "不合法的调用凭证")
    ,ERROR_40002(40002, "用户名或密码错误")
    ,ERROR_40003(40003, "没有符合条件的记录")
    ,ERROR_40004(40004, "不是有效的JSON格式数据")
    ,ERROR_40005(40005, "JSON格式不符合模板要求")
    ,ERROR_40006(40006, "不是有效的日期格式")
    ,ERROR_40007(40007, "数据主键缺失")
    ,ERROR_40008(40008, "超出一次写入上限")
    ,ERROR_40009(40009, "超出一次读取上限")
    ,ERROR_40014(40014, "不合法的access_token或已经过期")
    ,ERROR_40013(40013, "不合法的AppID")
    ,ERROR_41001(41001, "缺失access_token参数")
    ,ERROR_41002(41002, "缺失appid参数")
    ,ERROR_41003(41003, "参数错误")
    ,ERROR_41009(41009, "缺失openid参数")
    ,ERROR_42001(42001, "access_token已失效")
    ,ERROR_43001(43001, "需要使用GET方法请求")
    ,ERROR_43002(43002, "需要使用POST方法请求")
    ,ERROR_43003(43003, "需要使用HTTPS")
    ,ERROR_45005(45005, "url参数长度超过限制")
    ,ERROR_45006(45006, "picurl参数超过限制")
    ,ERROR_45007(45007, "播放时间超过限制（语音为60s最大）")
    ,ERROR_45009(45009, "接口调用频率超过限制")
    ,ERROR_50001(50001, "接口未授权")
    ,ERROR_50002(50002, "应用未授权")
    ,ERROR_50003(50003, "接口配置错误")
    ,ERROR_50004(50004, "应用未开启");

    private long code;
    private String msg;

    ResponseCode(final long code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

    public long getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


}
