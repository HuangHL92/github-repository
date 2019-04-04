package com.ruoyi.common.jsonIO;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.ruoyi.area.dataX.domain.dType.DsonJob;
import com.ruoyi.area.dataX.domain.DsonJobIn;
import com.ruoyi.common.config.Global;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;


/**
 * dataX工具类
 *
 * @Author: Zen
 * @Date: 2018/10/30 15:32
 */
public class DataXJsonCommon {
    /**
     * json文件夹路径
     */
    public static String JSON_PATH = Global.getJsonDataXPath();
    /**
     * datax的python文件地址
     */
    public static String DATAX_PATH = Global.getDataXExePath();
    /**
     * dataX log文件夹路径
     */
    public static String LOG_PATH = Global.getJsonDataXLogPath();

    /**
     * 删除dataXjson文件和log日志
     *
     * @param fileName
     */
    public static void delJsonAndLog(String fileName) {
        String jsonFilePath = JSON_PATH + fileName + ".json";
        String logFilePath = LOG_PATH + fileName + "_json.log";
        FileUtil.del(jsonFilePath);
        FileUtil.del(logFilePath);
    }

    /**
     * 读取Datax log内容
     *
     * @param fileName
     * @return
     * @throws IOException
     */

    public static String readToString(String fileName) {
        try {
            String filePath = LOG_PATH + fileName + "_json.log";
            File file = new File(filePath);
            // 获取文件长度
            Long fileLength = file.length();
            byte[] fileContent = new byte[fileLength.intValue()];

            FileInputStream in = new FileInputStream(file);
            in.read(fileContent);
            in.close();
            String fileContentS = new String(fileContent);
            // 返回文件内容,默认编码
            return fileContentS;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * 执行dataX作业
     *
     * @param fileName
     */
    public static String exeDataX(String fileName) {
        try {
            String line;
            String filePath = LOG_PATH + fileName + "_json.log";
            File file = new File(filePath);
            //文件不存在则创建文件，先创建目录
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
            //文件输出流用于将数据写入文件
            FileOutputStream outStream = new FileOutputStream(file);
            System.out.println("------------------start----------------------");
            /**
             * 执行Python命令
             */
            String windowCmd = "python27" + " " + DATAX_PATH + "datax.py" + " " + JSON_PATH + fileName + ".json";
            Process pr = Runtime.getRuntime().exec(windowCmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));

            while ((line = in.readLine()) != null) {
                outStream.write(line.getBytes());
                outStream.write("\n".getBytes());
                /**
                 * 打印输出日志
                 */
                System.out.println(line);
            }
            in.close();
            outStream.close();
            pr.waitFor();
            System.out.println("----------------end------------------");
            String log = readToString(fileName);
            return log;
        } catch (Exception e) {
            return e.getMessage();
        }

    }

    /**
     * 批量执行dataX作业
     */
    public static void exeDataxs() {
        try {
            StringBuilder logStr = new StringBuilder();
            System.out.println("------------------start----------------------");
            String[] str = getFileName(JSON_PATH);
            for (String name : str) {
                String windowcmd = "python27" + " " + DATAX_PATH + "datax.py" + " " + JSON_PATH + "/" + name + ".json";
                System.out.println(windowcmd);
                Process pr = Runtime.getRuntime().exec(windowcmd);
                BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
                in.close();
                pr.waitFor();
            }
            System.out.println("----------------end------------------");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 获取文件夹下所有 json 文件名
     *
     * @param path
     * @return
     */
    public static String[] getFileName(String path) {
        File file = new File(path);
        String[] fileName = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith(".json")) {
                    return true;
                }
                return false;
            }
        });
        return fileName;
    }

    public static Connection getConnection(String url, String name, String pwd) {
        String driver = "com.mysql.cj.jdbc.Driver";
        try {
            Class.forName(driver);
            //获取连接对象 链接 用户名 密码
            Connection conn = DriverManager.getConnection(url, name, pwd);
            conn.close();
            return conn;
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException:" + e.getMessage());
            return null;
        } catch (SQLException e) {
            System.out.println("SQLException:" + e.getMessage());
            return null;
        }


    }


    /**
     * 生成dataX做业基础模板只支持Mysql
     *
     * @param dsonJobIn
     */
    public static Boolean dataxJsonMod(DsonJobIn dsonJobIn) {
        //准备数据 开始
        String fileNames = dsonJobIn.getFileName() + ".json";
        String readerPort = "jdbc:mysql://" + dsonJobIn.getReaderPort() + "?characterEncoding=utf8&serverTimezone=GMT%2B8";
        String writerPort = "jdbc:mysql://" + dsonJobIn.getWriterPort() + "?characterEncoding=utf8&serverTimezone=GMT%2B8";
        String cols = dsonJobIn.getReaderColumn().replace('，', ',');
        String[] columns = cols.split(",");
        List<String> column = Arrays.asList(columns);
        List<DsonJob.Job.Content.Reader.Parameter.Connection> readerConnectionList = new ArrayList<>();
        DsonJob.Job.Content.Reader.Parameter.Connection readerConnection = new DsonJob.Job.Content.Reader.Parameter.Connection();
        List<String> readerTable = new ArrayList<>();
        //pojo
        readerTable.add(dsonJobIn.getReaderTableName());
        List<String> readerJdbcUrl = new ArrayList<>();
        readerConnectionList.add(readerConnection);
        //pojo
        readerJdbcUrl.add(readerPort);
        readerConnection.setTable(readerTable);
        readerConnection.setJdbcUrl(readerJdbcUrl);

        DsonJob.Job.Content.Reader.Parameter readerParameter = new DsonJob.Job.Content.Reader.Parameter();
        //pojo
        readerParameter.setUsername(dsonJobIn.getReaderUserName());
        //pojo
        readerParameter.setPassword(dsonJobIn.getReaderPassword());
        readerParameter.setColumn(column);
        //pojo
        readerParameter.setSplitPk(dsonJobIn.getSplitPk());
        readerParameter.setConnection(readerConnectionList);
        //pojo
        readerParameter.setWhere(dsonJobIn.getReaderWhere());


        List<DsonJob.Job.Content.Writer.Parameter.Connection> writerConnectionList = new ArrayList<>();
        DsonJob.Job.Content.Writer.Parameter.Connection writerConnection = new DsonJob.Job.Content.Writer.Parameter.Connection();
        List<String> writerTable = new ArrayList<>();
        //pojo
        writerTable.add(dsonJobIn.getReaderTableName());
        writerConnectionList.add(writerConnection);
        writerConnection.setTable(writerTable);
        writerConnection.setJdbcUrl(writerPort);

        DsonJob.Job.Content.Writer.Parameter writerParameter = new DsonJob.Job.Content.Writer.Parameter();
        List<String> writerPreSql = new ArrayList<>();
        //pojo
        writerPreSql.add(dsonJobIn.getPerSql());
        //pojo
        writerParameter.setWriteMode(dsonJobIn.getWriterMode());
        //pojo
        writerParameter.setUsername(dsonJobIn.getWriterUserName());
        //pojo
        writerParameter.setPassword(dsonJobIn.getWriterPassword());
        writerParameter.setColumn(column);
        writerParameter.setPreSql(writerPreSql);
        writerParameter.setConnection(writerConnectionList);

        DsonJob.Job.Content.Reader reader = new DsonJob.Job.Content.Reader();
        reader.setParameter(readerParameter);

        DsonJob.Job.Content.Writer writer = new DsonJob.Job.Content.Writer();
        writer.setParameter(writerParameter);

        DsonJob.Job.Content content = new DsonJob.Job.Content();
        content.setReader(reader);
        content.setWriter(writer);

        Map<String, Object> speedMap = new HashMap<>();
        speedMap.put(dsonJobIn.getSpeedByteName(), dsonJobIn.getSpeed());
        speedMap.put(dsonJobIn.getChannelName(), dsonJobIn.getChannel());
        //errorLimit
        DsonJob.Job.Setting.ErrorLimit errorLimit = new DsonJob.Job.Setting.ErrorLimit();
        //errorLimit
        DsonJob.Job.Setting setting = new DsonJob.Job.Setting();
        setting.setSpeed(speedMap);
        setting.setErrorLimit(errorLimit);

        List<DsonJob.Job.Content> contentList = new ArrayList<>();
        contentList.add(content);
        //准备数据 结束
        //判断数据库链接正确
        if (getConnection(readerPort, readerParameter.getUsername(), readerParameter.getPassword()) != null && getConnection(writerPort, writerParameter.getUsername(), writerParameter.getPassword()) != null) {
            //job
            DsonJob.Job job = new DsonJob.Job();
            job.setSetting(setting);
            job.setContent(contentList);

            DsonJob dsonJob = new DsonJob();
            dsonJob.setJob(job);


            createJson(dsonJob, fileNames);
//            ExportJson.readJson(fileNames);
            //连接正常
            return true;
        } else {
            //链接错误
            return false;
        }


    }

    /**
     * 创建文件
     *
     * @param object   对象
     * @param jsonFile 文件名
     */
    public static void createJson(Object object, String jsonFile) {
        //将对象格式化 json
        String jsonPrettyStr = JSONUtil.toJsonPrettyStr(object);
        // 文件夹路径
        if (FileUtil.exist(JSON_PATH + jsonFile)) {
            //存在删除文件
            FileUtil.del(JSON_PATH + jsonFile);
        }
        //创建文件重新写入
        FileUtil.writeString(jsonPrettyStr, (JSON_PATH + jsonFile), "UTF-8");
    }

}
