package com.ruoyi.common.jsonIO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ruoyi.common.config.Global;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @Author: Zen
 * @Date: 2018/10/29 11:54
 */
public class ExportJson {
    //读取Json文件
    public static void readJson(String fileName){
        String fileAddress = Global.getJsonPath()+fileName;
        String temp ;
        //从.json文件读取json字符串
        StringBuilder jsonStr = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileAddress))); //或者使用Scanner
            while((temp = reader.readLine())!= null)
                jsonStr.append(temp);
            reader.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
        //将json字符串转化为JSON对象，并读取内容
        try{
            JsonObject jsonObject = new JsonParser().parse(new JSONObject(jsonStr.toString()).toString()).getAsJsonObject();
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            System.out.println(gson.toJson(jsonObject));
        }catch(JSONException ex){
            ex.printStackTrace();
        }
    }

}
