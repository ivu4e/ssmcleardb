package com.weixiao.frame.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.weixiao.frame.base.JsonExclude;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class JsonUtils {

	/**
	 * 处理JsonConfig中需要排队的属性字段
	 * 
	 * @param object
	 *            要转换为json字符串的对象
	 * @param excludeList
	 *            保存需要排队的属性字段的List
	 */
	public static void handleJsonConfig(Object object, List<String> excludeList) {
		if (object == null) {
			return;
		}
		if (object instanceof Map) {
			// 如果是Map类型，则处理其Value;
			Map objectMap = (Map) object;
			for (Object value : objectMap.values()) {
				handleJsonConfig(value, excludeList);
			}
		} else if (object instanceof Collection) {
			// 如果是Collection类型，则处理其中一条数据;
			Collection collection = (Collection) object;
			if (collection.iterator().hasNext()) {
				handleJsonConfig(collection.iterator().next(), excludeList);
			}
		} else if (object instanceof GridDataModel) {
			GridDataModel gridDataModel = (GridDataModel) object;
			handleJsonConfig(gridDataModel.getRows(), excludeList);
		} else {
			// 处理本类属性上的JsonExclude排除注解
			Field[] fields = object.getClass().getDeclaredFields();
			for (Field f : fields) {
				if (null != f.getAnnotation(JsonExclude.class)) {
					if (!excludeList.contains(f.getName())) {
						excludeList.add(f.getName());
					}
				}
				try {
					if (f.get(object) instanceof Collection) {
						handleJsonConfig(f.get(object), excludeList);
					}
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
			}
			// 处理父类属性上的JsonExclude排除注解
			fields = object.getClass().getSuperclass().getDeclaredFields();
			for (Field f : fields) {
				if (null != f.getAnnotation(JsonExclude.class)) {
					if (!excludeList.contains(f.getName())) {
						excludeList.add(f.getName());
					}
				}
			}
		}
	}

	/**
	 * 根据要转换为json字符串的对象获取JsonConfig对象
	 * 
	 * @param object
	 *            要转换为json字符串的对象
	 * @return JsonConfig对象
	 */
	public static JsonConfig getJsonConfig(String[] propExclude, Object object) {
		List<String> excludeList = new ArrayList<String>();
		excludeList.add("createTime");
		excludeList.add("updateTime");
		excludeList.add("info");
		excludeList.add("status");
		excludeList.add("localId");
		if (propExclude != null && propExclude.length != 0) {
			for (String prop : propExclude) {
				if (!StringUtil.isBlank(prop)) {
					excludeList.add(prop);
				}
			}
		}
		handleJsonConfig(object, excludeList);
		JsonConfig jsonConfig = new JsonConfig();
		String[] excludeFileds = (String[]) excludeList.toArray(new String[excludeList.size()]);
		jsonConfig.setExcludes(excludeFileds);
		/**
		 * 注释此段代码，以下类型默认值将为 0；移动端对接接口时可以省去这些字段的 null 判断；
		 * jsonConfig.registerDefaultValueProcessor(Double.class, new
		 * DefaultDefaultValueProcessor() { public Object getDefaultValue(Class
		 * type) { return null; } });
		 * jsonConfig.registerDefaultValueProcessor(Float.class, new
		 * DefaultDefaultValueProcessor() { public Object getDefaultValue(Class
		 * type) { return null; } });
		 * jsonConfig.registerDefaultValueProcessor(Long.class, new
		 * DefaultDefaultValueProcessor() { public Object getDefaultValue(Class
		 * type) { return null; } });
		 * jsonConfig.registerDefaultValueProcessor(Integer.class, new
		 * DefaultDefaultValueProcessor() { public Object getDefaultValue(Class
		 * type) { return null; } });
		 */
		return jsonConfig;
	}

	/**
	 * 将对象转换为JSON格式字节数组
	 * 
	 * @author lipw
	 * @date 2014年8月29日上午11:26:46
	 * @param object
	 *            需要转换为JSON格式字节数组的对象
	 * @return JSON格式字节数组
	 */
	public static byte[] getJson(String[] propExclude, Object object) {
		byte[] jsonByte = new byte[] {};
		try {
			if (object != null) {
				if (object instanceof byte[]) {
					jsonByte = ((byte[]) object);
				} else if (object instanceof String) {
					jsonByte = (object.toString().getBytes("utf-8"));
				} else if (object instanceof Collection) {
					JsonConfig jsonConfig = getJsonConfig(propExclude, object);
					jsonByte = JSONArray.fromObject(object, jsonConfig).toString().getBytes("utf-8");
				} else {
					JsonConfig jsonConfig = getJsonConfig(propExclude, object);
					jsonByte = JSONObject.fromObject(object, jsonConfig).toString().getBytes("utf-8");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonByte;
	}

	/**
	 * 将N个对象转换为JSON格式存入一个字节数组
	 * 
	 * @author lipw
	 * @date 2014年8月29日下午12:30:52
	 * @param objects
	 *            需要转换为JSON格式字节数组的对象
	 * @return JSON格式字节数组
	 */
	public static byte[] getJson(String[] propExclude, Object... objects) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Integer i = 0;
		for (Object object : objects) {
			try {
				if (i != 0) {
					baos.write(new byte[] { ',' });
				}
				baos.write(getJson(propExclude, object));
				i++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return baos.toByteArray();
	}

	/**
	 * json 字符串转实体类,不支持内容属性集合
	 * 
	 * @author lipw
	 * @date 2017年5月10日下午6:08:24
	 * @param jsonString
	 * @param pojoCalss
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T jsonToObject(String jsonString, Class<T> pojoCalss) {
		if (StringUtil.isBlank(jsonString)) {
			return null;
		}
		try {
			Object pojo;
			net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(jsonString);
			pojo = net.sf.json.JSONObject.toBean(jsonObject, pojoCalss);
			return (T) pojo;
		} catch (Exception ex) {
			ex.printStackTrace();

			return null;
		}
	}

	/**
	 * json 字符串转集合
	 * 
	 * @author lipw
	 * @date 2017年5月10日下午6:08:17
	 * @param jsonString
	 * @param pojoCalss
	 *            集合中的类
	 * @return
	 */
	public static <T> List<T> jsonToList(String jsonString, Class<T> pojoCalss) {
		if (StringUtil.isBlank(jsonString)) {
			return null;
		}
		try {
			JSONArray jsonArray = JSONArray.fromObject(jsonString);
			// Java集合
			@SuppressWarnings("unchecked")
			List<T> list = (List<T>) JSONArray.toCollection(jsonArray, pojoCalss);
			return list;
		} catch (Exception ex) {
			ex.printStackTrace();

			return null;
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
