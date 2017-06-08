package com.weixiao.frame.util;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.weixiao.frame.base.ConstantDict;
import com.weixiao.frame.base.JsonExclude;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultDefaultValueProcessor;

/**
 * 封装与 HttpResponse 对象相关的实用功能 ResponseUtils
 * 
 * @author lipw
 * @date 2017年5月2日下午5:52:13
 */
public class ResponseUtils {

	// 失败未指定错误提示信息为-1
	protected static final String FAILURE = "-1";
	// 成功返回0
	protected static final String SUCCESS = "0";
	// 调用状态返回值的字段名称
	protected static final String RESULT_CODE = "code";
	// 调用成功后的结果使用此字段返回
	protected static final String RESULT_MESSAGE = "result";
	// 调用失败后的提示信息使用此字段返回
	protected static final String FAILURE_MESSAGE = "message";
	// 调用失败，如果有异常，使用此字段返回 StackTrace
	protected static final String EXCEPTION_MESSAGE = "stackTrace";

	/**
	 * 输出失败json {result: 'failure', error: '' }
	 * 
	 * @param response
	 * @throws IOException
	 */
	public static void outFailureJson(HttpServletResponse response) throws IOException {
		outFailureJson(response, "");
	}

	/**
	 * 输出失败json {result: 'failure', error: errMsg }
	 * 
	 * @param response
	 * @param errMsg
	 * @throws IOException
	 */
	public static void outFailureJson(HttpServletResponse response, String errMsg) throws IOException {
		outFailureJson(response, FAILURE, errMsg);
	}

	public static void outFailureJson(HttpServletResponse response, Long errCode, Object... args) throws IOException {
		String msg = ConstantDict.getInstance().getErrorMessage(errCode);
		if (msg != null) {
			msg = String.format(msg, args);
		}
		outFailureJson(response, errCode.toString(), msg);
	}

	/**
	 * 输出失败json {result: 'failure', error: errMsg }
	 * 
	 * @param response
	 * @param errCode
	 *            错误码
	 * @param errMsg
	 *            错误提示
	 * @throws IOException
	 */
	public static void outFailureJson(HttpServletResponse response, String errCode, String errMsg) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		map.put(RESULT_CODE, errCode);
		map.put(FAILURE_MESSAGE, errMsg);
		outJson(response, map);
	}

	/**
	 * 将传入的Object对象转换为JSON格式字符串作为响应内容以“utf-8”编码输出
	 * 
	 * @param response
	 * @param object
	 */
	public static void outJson(HttpServletResponse response, Object... objects) {
		// 设置响应内容编码，解决直接在浏览器地址栏访问中文内容乱码的问题
		response.setCharacterEncoding("utf-8");
		// 设置响应内容类型
		response.setContentType("application/json");
		try {
			OutputStream out = response.getOutputStream();
			Integer i = 0;
			for (Object object : objects) {
				try {
					if (i != 0) {
						out.write(new byte[] { ',' });
					}
					out.write(getJson(null, object));
					i++;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	private static byte[] getJson(String[] propExclude, Object object) {
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
	 * 根据要转换为json字符串的对象获取JsonConfig对象
	 * 
	 * @param object
	 *            要转换为json字符串的对象
	 * @return JsonConfig对象
	 */
	private static JsonConfig getJsonConfig(String[] propExclude, Object object) {
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
		jsonConfig.registerDefaultValueProcessor(Double.class, new DefaultDefaultValueProcessor() {
			public Object getDefaultValue(Class type) {
				return null;
			}
		});
		jsonConfig.registerDefaultValueProcessor(Float.class, new DefaultDefaultValueProcessor() {
			public Object getDefaultValue(Class type) {
				return null;
			}
		});
		jsonConfig.registerDefaultValueProcessor(Long.class, new DefaultDefaultValueProcessor() {
			public Object getDefaultValue(Class type) {
				return null;
			}
		});
		jsonConfig.registerDefaultValueProcessor(Integer.class, new DefaultDefaultValueProcessor() {
			public Object getDefaultValue(Class type) {
				return null;
			}
		});
		return jsonConfig;
	}

	/**
	 * 处理JsonConfig中需要排队的属性字段
	 * 
	 * @param object
	 *            要转换为json字符串的对象
	 * @param excludeList
	 *            保存需要排队的属性字段的List
	 */
	private static void handleJsonConfig(Object object, List<String> excludeList) {
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

}
