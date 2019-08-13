package com.ruoyi.base;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.base.ApiResult;
import com.ruoyi.common.enums.ResponseCode;
import com.ruoyi.common.exception.ApiRuntimeException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.entity.DataSchema;
import com.ruoyi.utils.CacheUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Description $功能描述$
 * @Author yufei
 * @Date 2019-03-06 09:20
 **/
public class ApiBaseController {

    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());


    // 访问日志
    public static final String LOG_IO = "1";
    // 任务日志
    public static final String LOG_TASK = "2";
    // 查询日志
    public static final String LOG_QUERY = "3";
    // 操作日志
    public static final String LOG_OPERATION = "4";



    public ApiBaseController() {
    }

    protected <T> ApiResult<T> success() {
        return ApiResult.success(null);
    }

    protected <T> ApiResult<T> success(T data) {
        return ApiResult.success(data);
    }

    protected <T> ApiResult<T> success(String msg, T data) {
        return ApiResult.success(msg,data);
    }

    protected <T> ApiResult<T> error(String msg) {
        return ApiResult.error(msg);
    }

    protected <T> ApiResult<T> error(ResponseCode errorCode) {
        return ApiResult.error(errorCode);
    }

    /**
     * <p>
     * 自定义 REST 业务异常
     * <p>
     *
     * @param e 异常类型
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ApiResult<Object> handleBadRequest(Exception e) {

        /*
         * 业务逻辑异常
         */
        if (e instanceof ApiRuntimeException) {
            ResponseCode errorCode = ((ApiRuntimeException) e).getErrorCode();
            if (null != errorCode) {
                logger.debug("Rest request error, {}", errorCode.toString());
                return ApiResult.error(errorCode);
            }
            logger.debug("Rest request error, {}", e.getMessage());
            return ApiResult.error(e.getMessage());
        }

        /*
         * 参数校验异常
         */
        if (e instanceof BindException) {
            BindingResult bindingResult = ((BindException) e).getBindingResult();
            if (null != bindingResult && bindingResult.hasErrors()) {
                List<Object> jsonList = new ArrayList<>();
                bindingResult.getFieldErrors().stream().forEach(fieldError -> {
                    Map<String, Object> jsonObject = new HashMap<>(2);
                    jsonObject.put("name", fieldError.getField());
                    jsonObject.put("msg", fieldError.getDefaultMessage());
                    jsonList.add(jsonObject);
                });
                return ApiResult.restResult(jsonList, ResponseCode.FAILED);
            }
        }

        /*
         * 其他异常
         */
        if (e instanceof Exception) {
            logger.debug("Rest request error, {}", e.getMessage());
            return ApiResult.error(e.getMessage());
        }


        /**
         * 系统内部异常，打印异常栈
         */
        logger.error("Error: handleBadRequest StackTrace : {}", e);

        return ApiResult.error(ResponseCode.FAILED,e.getMessage());
    }



    /***
     * 最后执行处理
     * @param logType 日志类型
     * @param appid 应用ID （appid）
     * @param sourceid 资源ID
     * @param taskid 任务ID
     * @param msg 消息
     */
    public void writeLog(String logType, String appid, String sourceid, String taskid, String msg) {

        // 日志输出到控制台
        logger.info(msg);

    }



//    /***
//     * 格式化返回值
//     * @param app 配置信息
//     * @param datastoreList 原始数据
//     */
//    public JSONArray formatResult(TApp app, List<TDatastore> datastoreList) {
//
//        DBUtils.initDataSource("db0");
//        List<TAppdetail> details = appdetailService.selectList(new QueryWrapper<TAppdetail>().eq("t_app_id",app.getId()));
//
//        // 6.根据fields截取指定json(只给出申请审核通过的字段）
//        JSONArray jsonArray = new JSONArray();
//        for (TDatastore datastore : datastoreList) {
//            String dataValue = datastore.getDatavalue();
////            net.sf.json.JSONObject jsonObjectFrom = net.sf.json.JSONObject.fromObject(dataValue);
////            net.sf.json.JSONObject jsonObjectTo = new net.sf.json.JSONObject();
//            for (TAppdetail detail : details) {
////                jsonObjectTo.put(detail.getNameEn(), jsonObjectFrom.get(detail.getNameEn()));
//            }
////            jsonArray.add(jsonObjectFrom);
//        }
//        return  jsonArray;
//    }

    /***
     * 创建Insert语句
     * @param tablename
     * @param dataSchema
     * @param dataobj
     * @param pk
     * @return
     */
    public String createInsertSQL(String tablename, DataSchema dataSchema, Object dataobj, String pk) {

        StringBuffer values = new StringBuffer();
        StringBuffer keys = new StringBuffer();
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO " + tablename + " (");

        //业务字段
        for(String key : ((JSONObject) dataobj).keySet()){
            keys.append(key).append(",");
            Object val =((JSONObject) dataobj).get(key);
            if(val==null || "null".equals(val) || StringUtils.isEmpty(val.toString().trim())){
                values.append("null").append(",");
            } else {
                values.append("'").append(val).append("'").append(",");
            }

        }


        String nowTime = DateUtils.dateTimeNow("yyyy-MM-dd HH:mm:ss");

        //常规字段
        keys.append("id").append(",");
        values.append("'").append(pk).append("'").append(",");
        keys.append("appid").append(",");
        values.append("'").append(dataSchema.getAppid()).append("'").append(",");
        keys.append("oid").append(",");
        values.append("'").append(dataSchema.getPk()).append("'").append(","); //TODO 测试
        keys.append("ojson").append(",");
        values.append("'").append(JSONObject.toJSONString(dataobj)).append("'").append(",");
        keys.append("odate").append(","); //TODO 测试
        values.append("'").append(dateFormat(dataSchema.getUpdatetime())).append("'").append(",");
        keys.append("create_date").append(",");
        values.append("'").append(nowTime).append("'").append(",");
        keys.append("create_by").append(",");
        values.append("'").append(dataSchema.getCreator()).append("'").append(",");
        keys.append("update_date");
        values.append("'").append(nowTime).append("'");


        String str_key = keys.toString();
        String str_value = values.toString();
        sql.append(str_key).append(") values(");
        sql.append(str_value).append(")");
        sql.append(" on duplicate key update oid='" + dataSchema.getPk()+ "'");
        logger.info(sql.toString());
        return sql.toString();
    }


    /***
     * 创建更新语句
     * @param tablename
     * @param dataSchema
     * @param dataobj
     * @param pk
     * @return
     */
    public String createUpdateSQL(String tablename, DataSchema dataSchema, Object dataobj, String pk) {

        StringBuffer values = new StringBuffer();
        StringBuffer keys = new StringBuffer();
        StringBuffer sql = new StringBuffer();
        sql.append("Update " + tablename + " set ");

        for(String key : ((JSONObject) dataobj).keySet()){
            Object value=((JSONObject) dataobj).get(key);
            if(value==null || "null".equals(value) || StringUtils.isEmpty(value.toString().trim())){
                sql.append(key).append("=null").append(",");
            } else {

                sql.append(key).append("='").append(value).append("'").append(",");
            }
        }

        //常规字段
        sql.append("ojson='").append(JSONObject.toJSONString(dataobj)).append("'").append(",");
        sql.append("odate='").append(dateFormat(dataSchema.getUpdatetime())).append("'").append(",");
        sql.append("update_date='").append(DateUtils.dateTimeNow("yyyy-MM-dd HH:mm:ss")).append("'");
        sql.append(" where oid='").append(dataSchema.getPk()).append("'");
        //如果数据有多个提供方式
//        if(StringUtils.isNotEmpty(dataSchema.getCreator())) {
//            sql.append(" and create_by='").append(dataSchema.getCreator()).append("'");
//        }

        logger.info(sql.toString());
        return sql.toString();
    }

    /***
     * 创建删除语句
     * @param tablename
     * @param pk
     * @return
     */
    public String createDelSQL(String tablename, String pk, String creator) {

        StringBuffer sql = new StringBuffer();
        sql.append("Delete From " + tablename + " Where oid='" + pk + "'");
        if(StringUtils.isNotEmpty(creator)) {
            sql.append(" and create_by='" + creator + "'");
        }
        logger.info(sql.toString());
        return sql.toString();
    }


    /***
     * 创建查询语句
     * @param tablename
     * @param pk
     * @return
     */
    public String createSelectSQL(String tablename, String pk, String creator) {

        StringBuffer sql = new StringBuffer();
        sql.append("Select oid From " + tablename + " Where oid='" + pk + "'");
        if(StringUtils.isNotEmpty(creator)) {
            sql.append(" and create_by='" + creator + "'");
        }
        return sql.toString();
    }


    private String dateFormat(String datetime) {
        String nowTime = DateUtils.dateTimeNow("yyyy-MM-dd HH:mm:ss");
        if(StringUtils.isEmpty(datetime)) {
            return nowTime;
        } else {
            try {
                //时间戳处理
                datetime = DateFormatUtils.format(new Date(datetime),"yyyy-MM-dd HH:mm:ss");
            } catch (Exception ex) {
                //
            }
        }
        return datetime;
    }

}
