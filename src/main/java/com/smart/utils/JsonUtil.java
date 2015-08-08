package com.smart.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * json操作集合类
 * 
 * @author gaowm
 * 
 * @date 2012-6-28 下午2:25:27
 * 
 */
public class JsonUtil implements Serializable {
	private static final long serialVersionUID = 1L;

	public static Object getObject4JsonString(String jsonString, Class pojoCalss) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		Object pojo = JSONObject.toBean(jsonObject, pojoCalss);
		return pojo;
	}

	public static Map getMap4Json(String jsonString) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		Iterator keyIter = jsonObject.keys();

		Map valueMap = new HashMap();

		while (keyIter.hasNext()) {
			String key = (String) keyIter.next();
			Object value = jsonObject.get(key);
			valueMap.put(key, value);
		}

		return valueMap;
	}

	public static Object[] getObjectArray4Json(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		return jsonArray.toArray();
	}

	public static List getList4Json(String jsonString, Class pojoClass) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);

		List list = new ArrayList();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Object pojoValue = JSONObject.toBean(jsonObject, pojoClass);
			list.add(pojoValue);
		}

		return list;
	}

	public static String[] getStringArray4Json(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		String[] stringArray = new String[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			stringArray[i] = jsonArray.getString(i);
		}

		return stringArray;
	}

	public static Long[] getLongArray4Json(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		Long[] longArray = new Long[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			longArray[i] = Long.valueOf(jsonArray.getLong(i));
		}

		return longArray;
	}

	public static Integer[] getIntegerArray4Json(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		Integer[] integerArray = new Integer[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			integerArray[i] = Integer.valueOf(jsonArray.getInt(i));
		}

		return integerArray;
	}

	public static String getJsonString4JavaPOJO(Object javaObj) {
		JSONObject json = JSONObject.fromObject(javaObj);
		return json.toString();
	}

	public static String getJsonString4ListData(List<?> list) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if ((list != null) && (list.size() > 0)) {
			for (Iterator i$ = list.iterator(); i$.hasNext();) {
				Object obj = i$.next();
				json.append(getJsonString4JavaPOJO(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}
}