package com.ruoyi.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.enums.ResponseCode;
import com.ruoyi.common.exception.ApiRuntimeException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class JsonUtils {

	/**
	 * 比对json和模板的区别
	 * @param json
	 * @param templet
	 * @return
	 */
	public static boolean compare(String json,String templet) throws ApiRuntimeException {
		ArrayList<String> jsonKeyList = getJsonKeys(null, JSONObject.parseObject(json));
		ArrayList<String> templetKeyList = getJsonKeys(null, JSONObject.parseObject(templet));
		int sizeDiff = jsonKeyList.size()-templetKeyList.size();
		StringBuilder builder = new StringBuilder();
		boolean isContain = true;
		for (String string : jsonKeyList) {
			if(!templetKeyList.contains(string)){
				isContain = false;
				builder.append(string).append(",");
			}
		}
		if (sizeDiff!=0) {
			throw new ApiRuntimeException(ResponseCode.ERROR_40005);
		}
		
		if (!isContain) {
			System.out.println("MSG: 不包含参数  "+builder.toString());
			throw new ApiRuntimeException(ResponseCode.ERROR_40005, "JSON格式不符合模板要求  参数："+builder.toString());
		}	
		
		if (sizeDiff==0&&isContain) {
			System.out.println("MSG: json format is right!");
		}
		return sizeDiff==0||isContain;
	}
	
	
	/**
	 * 获取json所有的key的集合
	 * @param head
	 * @param jsonObj
	 * @return
	 */
	public static ArrayList<String> getJsonKeys(String head,JSONObject jsonObj){
		ArrayList<String> keys = new ArrayList<>();
		Set<Entry<String, Object>> set =  jsonObj.entrySet();
		Iterator<Entry<String, Object>> iter = set.iterator();
		while (iter.hasNext()) {
			Entry<String, Object> entry = iter.next();
			String key = entry.getKey();
			Object value = entry.getValue();
			
			keys.add(head==null?key:head+key);
			//判断value类型
			if (value instanceof JSONObject) {
				ArrayList<String> list = getJsonKeys(head==null?key+"-":head+key+"-", (JSONObject) value);
				keys.addAll(list);
			}else if (value instanceof JSONArray) {
				JSONArray jsonArray = (JSONArray) value;
				if (jsonArray.size()!=0) {
					Object obj = jsonArray.get(0);
					//对array中的类型进行判断
					if (obj instanceof JSONObject) {
						ArrayList<String> list = getJsonKeys(head==null?key+"-":head+key+"-", (JSONObject)jsonArray.get(0));
						keys.addAll(list);
					}else{
						keys.add(head==null?key:head+key);
					}
				}
			}
		}
		return keys;
	}



}
