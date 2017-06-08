package com.weixiao.frame.base;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.weixiao.frame.page.Page;
import com.weixiao.frame.page.PageRequest;
import com.weixiao.frame.util.BaseDataUtil;
import com.weixiao.frame.util.DateUtils;
import com.weixiao.frame.util.GridDataModel;
import com.weixiao.frame.util.JsonUtils;
import com.weixiao.frame.util.SpringContextUtil;

public class BaseAdminController extends BaseSpringController {
	// 默认的排序方式
	protected static final String DEFAULT_SORT_COLUMNS = " id asc";
	// 排序条件的Key名称，要指定排序条件，需要在 Map filters = new HashMap();中添加此Key和对应的Value
	protected static final String SORT_COLUMNS = "sortColumns";
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
	 * 设置分页信息：使用默认参数名“limit, start”生成PageRequest<Map>对象
	 * 
	 * @param request
	 * @return
	 */
	public PageRequest<Map> setPageValue(HttpServletRequest request) {
		PageRequest<Map> pageRequest = newPageRequest(request, DEFAULT_SORT_COLUMNS);
		String startStr = request.getParameter("start") == null ? "0" : request.getParameter("start").trim();
		String limitStr = request.getParameter("limit") == null ? "10" : request.getParameter("limit").trim();
		int start = Integer.parseInt(startStr);
		int limit = Integer.parseInt(limitStr);
		int pageNumber = 1;
		pageNumber = start / limit + 1;
		pageRequest.setPageSize(limit);
		pageRequest.setPageNumber(pageNumber);
		return pageRequest;
	}

	
	/**
	 * 将传入的Object对象转换为JSON格式字符串作为响应内容以“utf-8”编码输出
	 * 
	 * @param response
	 * @param object
	 */
	public void outJson(HttpServletResponse response, Object... objects) {
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
					out.write(JsonUtils.getJson(null, object));
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
	 * 将传入的Object对象转换为JSON格式字符串作为响应内容以“utf-8”编码输出
	 * 
	 * @param response
	 * @param object
	 */
	public void outJsonEx(HttpServletResponse response, String[] propExclude, Object... objects) {
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
					out.write(JsonUtils.getJson(propExclude, object));
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
	 * 输出Page json
	 * 
	 * @param response
	 * @param page
	 * @param isInitBaseFieldName
	 *            是否对base字段赋值
	 */
	public void outPageJson(HttpServletResponse response, Page page, boolean isInitBaseFieldName) {
		if (isInitBaseFieldName) {
			BaseDataUtil.setBaseFieldName(page.getResult());
		}
		GridDataModel model = new GridDataModel();
		model.setRows(page.getResult());
		model.setTotal(page.getTotalCount());
		this.outJson(response, model);
	}

	/**
	 * 输出Page json
	 * 
	 * @param response
	 * @param page
	 * @param isInitBaseFieldName
	 *            是否对base字段赋值
	 */
	public void outPageJsonEx(HttpServletResponse response, Page page, boolean isInitBaseFieldName,
			String[] propExclude) {
		if (isInitBaseFieldName) {
			BaseDataUtil.setBaseFieldName(page.getResult());
		}
		GridDataModel model = new GridDataModel();
		model.setRows(page.getResult());
		model.setTotal(page.getTotalCount());
		this.outJsonEx(response, propExclude, model);
	}

	/**
	 * 输出Object json
	 * 
	 * @param response
	 * @param obj
	 *            Model
	 * @param isInitBaseFieldName
	 *            是否对base字段赋值
	 */
	public void outObjectJson(HttpServletResponse response, Object obj, boolean isInitBaseFieldName) {
		if (isInitBaseFieldName) {
			BaseDataUtil.setFieldName(obj);
		}
		this.outJson(response, obj);
	}

	/**
	 * 输出成功json {result: 'success'}
	 * 
	 * @param response
	 * @throws IOException
	 */
	public void outSuccessJson(HttpServletResponse response) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		map.put(RESULT_CODE, SUCCESS);
		this.outJson(response, map);
	}

	/**
	 * 输出失败json {result: 'failure', error: '' }
	 * 
	 * @param response
	 * @throws IOException
	 */
	public void outFailureJson(HttpServletResponse response) throws IOException {
		outFailureJson(response, "");
	}

	/**
	 * 输出失败json {result: 'failure', error: errMsg }
	 * 
	 * @param response
	 * @param errMsg
	 * @throws IOException
	 */
	public void outFailureJson(HttpServletResponse response, String errMsg) throws IOException {
		outFailureJson(response, FAILURE, errMsg);
	}

	public void outFailureJson(HttpServletResponse response, Long errCode, Object... args) throws IOException {
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
	public void outFailureJson(HttpServletResponse response, String errCode, String errMsg) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		map.put(RESULT_CODE, errCode);
		map.put(FAILURE_MESSAGE, errMsg);
		this.outJson(response, map);
	}

	/**
	 * json输出 {result:result} json格式的结果
	 * 
	 * @param response
	 * @param obj
	 * @throws IOException
	 */
	public void outResultJson(HttpServletResponse response, Object obj) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(RESULT_CODE, SUCCESS);
		map.put(RESULT_MESSAGE, obj);
		this.outJson(response, map);
	}

	public void outResultJson(HttpServletResponse response, Object obj, String key, Object value) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(RESULT_CODE, SUCCESS);
		map.put(RESULT_MESSAGE, obj);
		map.put(key, value);
		this.outJson(response, map);
	}

	public void outResultJson(HttpServletResponse response, Object obj, String key, Object value, String key1,
			Object value1) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(RESULT_CODE, SUCCESS);
		map.put(RESULT_MESSAGE, obj);
		map.put(key, value);
		map.put(key1, value1);
		this.outJson(response, map);
	}

	/**
	 * json输出 {result:result} json格式的结果
	 * 
	 * @param response
	 * @param obj
	 * @param propExclude 要排除的字段
	 * @throws IOException
	 */
	public void outResultJson(HttpServletResponse response, Object obj, String[] propExclude) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(RESULT_CODE, SUCCESS);
		map.put(RESULT_MESSAGE, obj);
		this.outJsonEx(response, propExclude, map);
	}

	/**
	 * json输出 {result:result} json格式的结果
	 * @author lipw
	 * @date   2017年5月8日下午3:18:55
	 * @param response
	 * @param obj
	 * @param propExclude 要排除的字段
	 * @param key
	 * @param value
	 */
	public void outResultJson(HttpServletResponse response, Object obj, String[] propExclude, String key, Object value) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(RESULT_CODE, SUCCESS);
		map.put(RESULT_MESSAGE, obj);
		map.put(key, value);
		this.outJsonEx(response, propExclude, map);
	}

	/**
	 * json输出 {result:result} json格式的结果
	 * @author lipw
	 * @date   2017年5月8日下午3:19:26
	 * @param response
	 * @param obj
	 * @param propExclude 要排除的字段
	 * @param key
	 * @param value
	 * @param key1
	 * @param value1
	 */
	public void outResultJson(HttpServletResponse response, Object obj, String[] propExclude, String key, Object value, String key1,
			Object value1) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(RESULT_CODE, SUCCESS);
		map.put(RESULT_MESSAGE, obj);
		map.put(key, value);
		map.put(key1, value1);
		this.outJsonEx(response, propExclude, map);
	}
	
	/**
	 * 国际化的json输出 {result:message} 输出result:result json格式的结果
	 * 对应文件message.properties message_zh.properties
	 * 
	 * @param response
	 * @param messageKey
	 * @throws IOException
	 */
	public void outResultJsoni18n(HttpServletResponse response, String messageKey) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		String str = SpringContextUtil.getMessage(messageKey);
		map.put(RESULT_MESSAGE, str);
		map.put("error", str);
		this.outJson(response, map);
	}

	public void outResultJsoni18n(HttpServletResponse response, String messageKey, String elementName)
			throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		map.put(elementName, SpringContextUtil.getMessage(messageKey));
		this.outJson(response, map);
	}

	/**
	 * 国际化的json输出 {result:message} 输出result:result json格式的结果
	 * 对应文件message.properties message_zh.properties
	 * 
	 * @param response
	 * @param messageKey
	 * @param args
	 * @param locale
	 *            传人null,则为中文
	 * @throws IOException
	 */
	public void outResultJsoni18n(HttpServletResponse response, String messageKey, Object[] args, Locale locale)
			throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		if (locale == null) {
			locale = new Locale("zh", "CN");
		}
		String str = SpringContextUtil.getMessage(messageKey, args, locale);
		map.put(RESULT_MESSAGE, str);
		map.put("error", str);
		this.outJson(response, map);
	}

	/**
	 * 获得Timestamp类型的参数 如果不存在 则返回当前时间
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	public Timestamp getTimestamp(HttpServletRequest request, String name) {
		String date = get(name);
		if (StringUtils.isNotBlank(date)) {
			if (date.length() < 11) {
				date += " 00:00:00";
			}
			return Timestamp.valueOf(date);
		}
		return Timestamp.valueOf(DateUtils.getStringDate());
	}

	/**
	 * 把request对应的参数赋值到对象中
	 * 赋值字段类型：String、Long、Integer、Double、Float、Timestamp、Boolean（1：true 0：false）
	 * 
	 * @param object
	 * @param request
	 * @param isEdit
	 *            是否编辑模式，如果编辑模式，则忽略null或不存在对应字段的赋值
	 */
	public void setFieldValues(Object object, HttpServletRequest request, boolean isEdit) {
		if (object == null)
			return;
		Field[] fields = object.getClass().getDeclaredFields();
		String simpleName = "";
		try {
			for (Field f : fields) {
				if (f.getModifiers() > 2) {
					continue;
				}
				f.setAccessible(true);
				simpleName = f.getType().getSimpleName();
				if (simpleName.equals("String")) {
					if (this.get(f.getName()) == null && isEdit) {
						continue;
					}
					f.set(object, this.get(f.getName()));
					continue;
				} else if (simpleName.equals("Long")) {
					if (this.getLong(f.getName()) == null && isEdit) {
						continue;
					}
					f.set(object, this.getLong(f.getName()));
					continue;
				} else if (simpleName.equals("Integer")) {
					if (this.getInteger(f.getName()) == null && isEdit) {
						continue;
					}
					f.set(object, this.getInteger(f.getName()));
					continue;
				} else if (simpleName.equals("Boolean")) {
					if (this.get(f.getName()) == null && isEdit) {
						continue;
					}
					Boolean bool = ("1".equals(this.get(f.getName())) || "true".equals(this.get(f.getName())));
					f.set(object, bool);
					continue;
				} else if (simpleName.equals("Timestamp")) {
					if (StringUtils.isBlank(this.get(f.getName())) && isEdit) {
						continue;
					}
					f.set(object, this.getTimestamp(request, f.getName()));
					continue;
				} else if (simpleName.equals("Double")) {
					if (StringUtils.isNotBlank(this.get(f.getName()))) {
						f.set(object, Double.valueOf(this.get(f.getName())));
					}
					continue;
				} else if (simpleName.equals("Float")) {
					if (StringUtils.isNotBlank(this.get(f.getName()))) {
						f.set(object, Float.valueOf(this.get(f.getName())));
					}
					continue;
				} else if (simpleName.equals("Byte")) {
					if (StringUtils.isNotBlank(this.get(f.getName()))) {
						f.set(object, Byte.valueOf(this.get(f.getName())));
					}
					continue;
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 把request对应的参数赋值到对象中
	 * 赋值字段类型：String、Long、Integer、Double、Float、Timestamp、Boolean（1：true 0：false）
	 * 
	 * @param object
	 * @param request
	 * @param isEdit
	 *            是否编辑模式，如果编辑模式，则忽略null或不存在对应字段的赋值
	 */
	public void setFieldValues(Object object, MultipartHttpServletRequest multiRequest, boolean isEdit) {
		if (object == null)
			return;
		Field[] fields = object.getClass().getDeclaredFields();
		String simpleName = "";
		try {
			for (Field f : fields) {
				if (f.getModifiers() > 2) {
					continue;
				}
				f.setAccessible(true);
				simpleName = f.getType().getSimpleName();
				if (simpleName.equals("String")) {
					if (this.get(multiRequest, f.getName()) == null && isEdit) {
						continue;
					}
					f.set(object, this.get(multiRequest, f.getName()));
					continue;
				} else if (simpleName.equals("Long")) {
					if (this.getLong(multiRequest, f.getName()) == null && isEdit) {
						continue;
					}
					f.set(object, this.getLong(multiRequest, f.getName()));
					continue;
				} else if (simpleName.equals("Integer")) {
					if (this.getInteger(multiRequest, f.getName()) == null && isEdit) {
						continue;
					}
					f.set(object, this.getInteger(multiRequest, f.getName()));
					continue;
				} else if (simpleName.equals("Boolean")) {
					if (this.get(multiRequest, f.getName()) == null && isEdit) {
						continue;
					}
					Boolean bool = ("1".equals(this.get(multiRequest, f.getName())) || "true".equals(this.get(multiRequest, f.getName())));
					f.set(object, bool);
					continue;
				} else if (simpleName.equals("Timestamp")) {
					if (StringUtils.isBlank(this.get(multiRequest, f.getName())) && isEdit) {
						continue;
					}
					f.set(object, this.getTimestamp(multiRequest, f.getName()));
					continue;
				} else if (simpleName.equals("Double")) {
					if (StringUtils.isNotBlank(this.get(multiRequest, f.getName()))) {
						f.set(object, Double.valueOf(this.get(multiRequest, f.getName())));
					}
					continue;
				} else if (simpleName.equals("Float")) {
					if (StringUtils.isNotBlank(this.get(multiRequest, f.getName()))) {
						f.set(object, Float.valueOf(this.get(multiRequest, f.getName())));
					}
					continue;
				} else if (simpleName.equals("Byte")) {
					if (StringUtils.isNotBlank(this.get(multiRequest, f.getName()))) {
						f.set(object, Byte.valueOf(this.get(multiRequest, f.getName())));
					}
					continue;
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param object
	 * @param isIgnoreNull
	 * @return
	 */
	public Map getPropertiesMap(Object bean, boolean isIgnoreNull) {
		Map map = new HashMap();
		if (bean == null)
			return map;
		Field[] fields = bean.getClass().getDeclaredFields();
		Field.setAccessible(fields, true);
		Object obj = null;
		for (int i = 0; i < fields.length; i++) {
			try {
				obj = fields[i].get(bean);
				if (isIgnoreNull && obj == null) {
					continue;
				}
				map.put(fields[i].getName(), obj);
			} catch (IllegalArgumentException e) {
				log.error(e.getMessage());
			} catch (IllegalAccessException e) {
				log.error(e.getMessage());
			}
		}
		return map;
	}

	/**
	 * 根据属性名获得对应的属性值
	 * 
	 * @param bean
	 * @param fieldName
	 * @return
	 */
	public Object getProperty(Object bean, String fieldName) {
		Field[] fields = bean.getClass().getDeclaredFields();
		Field.setAccessible(fields, true);
		Object obj = null;
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			if (fieldName.equals(field.getName())) {
				try {
					obj = field.get(bean);
				} catch (IllegalArgumentException e) {
					log.error(e.getMessage());
				} catch (IllegalAccessException e) {
					log.error(e.getMessage());
				}
			}
		}
		return obj;
	}
}
