package com.weixiao.frame.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.weixiao.frame.page.Page;
import com.weixiao.frame.page.PageRequest;


/**
 * Spring控制器基类，注入当前的request对象，同时提供常用方法的封装.
 * <p>
 * 子类必须添加<code>@Scope("prototype")</code>注解.
 * </p>
 * 
 * @author lipw
 *
 */
public abstract class BaseSpringController {

	protected Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	protected HttpServletRequest request;
	
	protected <T> T copyProperties(final Class<T> destClass, final Object orig) {
		return BeanUtils.copyProperties(destClass, orig);
	}

	protected void copyProperties(final Object target, final Object source) {
		BeanUtils.copyProperties(target, source);
	}

	/**
	 * 重定向到操作失败页.
	 */
	
	public void fail(final String msg, final boolean needRrefeshTree, final Exception e) {
		Result.fail(msg, get("backUrl"), needRrefeshTree, e);
	}
	public void fail(final String msg,  final Exception e) {
		Result.fail(msg, get("backUrl"), false, e);
	}
	public void fail(final String msg) {
		Result.fail(msg, get("backUrl"), false, null);
	}

	
	/**
	 * 等价于request.getParameter(name).
	 */
	protected String get(final String name) {
		return get(name, null);
	}
	
	protected String get(String name, String defaultValue) {
		String value = request.getParameter(name);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
	
	/**
	 * 等价于request.getParameter(name).
	 */
	protected String get(final MultipartHttpServletRequest multiRequest, final String name) {
		return get(multiRequest, name, null);
	}
	
	protected String get(final MultipartHttpServletRequest multiRequest, String name, String defaultValue) {
		String value = multiRequest.getParameter(name);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
	
	/**
	 * 如果为MultipartHttpServletRequest，获取name指定的MultipartFile，否则返回null.
	 */
	protected MultipartFile getFile(final String name) {
		ServletRequestAttributes requestAttributes = 
			(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		final HttpServletRequest realRequest = requestAttributes.getRequest();
		if (realRequest instanceof MultipartHttpServletRequest) {
			final MultipartHttpServletRequest multipartRequset = 
				(MultipartHttpServletRequest) realRequest;
			return multipartRequset.getFile(name);
		}
		return null;
	}

	protected Map<String, MultipartFile> getFileMap() {
		ServletRequestAttributes requestAttributes = 
			(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		final HttpServletRequest realRequest = requestAttributes.getRequest();
		if (realRequest instanceof MultipartHttpServletRequest) {
			final MultipartHttpServletRequest multipartRequset = (MultipartHttpServletRequest) realRequest;
			return multipartRequset.getFileMap();
		}
		return null;
	}

	/**
	 * 如果为MultipartHttpServletRequest，获取name指定的MultipartFile列表，否则返回null.
	 */
	protected List<MultipartFile> getFiles(final String name) {
		ServletRequestAttributes requestAttributes = 
			(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		final HttpServletRequest realRequest = requestAttributes.getRequest();
		if (realRequest instanceof MultipartHttpServletRequest) {
			final MultipartHttpServletRequest multipartRequset = (MultipartHttpServletRequest) realRequest;
			return multipartRequset.getFiles(name);
		}
		return null;
	}

	protected Integer getInteger(final String name) {
		final String str = request.getParameter(name);
		if (StringUtils.isNotBlank(str)) {
			return Integer.valueOf(str);
		}
		return null;
	}
	
	protected Integer getInteger(final MultipartHttpServletRequest multiRequest, final String name) {
		final String str = multiRequest.getParameter(name);
		if (StringUtils.isNotBlank(str)) {
			return Integer.valueOf(str);
		}
		return null;
	}
	protected Double getDouble(final String name) {
		final String str = request.getParameter(name);
		if (StringUtils.isNotBlank(str)) {
			return Double.valueOf(str);
		}
		return null;
	}
	
	protected Double getDouble(final MultipartHttpServletRequest multiRequest, final String name) {
		final String str = multiRequest.getParameter(name);
		if (StringUtils.isNotBlank(str)) {
			return Double.valueOf(str);
		}
		return null;
	}
	
	protected Long getLong(final String name) {
		final String str = request.getParameter(name);
		if (StringUtils.isNotBlank(str)) {
			return Long.valueOf(str);
		}
		return null;
	}

	protected Long getLong(final MultipartHttpServletRequest multiRequest, final String name) {
		final String str = multiRequest.getParameter(name);
		if (StringUtils.isNotBlank(str)) {
			return Long.valueOf(str);
		}
		return null;
	}
	
	protected MultiValueMap<String, MultipartFile> getMultiFileMap() {
		ServletRequestAttributes requestAttributes = 
			(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		final HttpServletRequest realRequest = requestAttributes.getRequest();
		if (realRequest instanceof MultipartHttpServletRequest) {
			final MultipartHttpServletRequest multipartRequset = 
				(MultipartHttpServletRequest) realRequest;
			return multipartRequset.getMultiFileMap();
		}
		return null;
	}

	protected <T> T getOrCreateRequestAttribute(final String key,
			final Class<T> clazz) {
		Object value = request.getAttribute(key);
		if (value == null) {
			try {
				value = clazz.newInstance();
			} catch (final Exception e) {
				ReflectionUtils.handleReflectionException(e);
			}
			request.setAttribute(key, value);
		}
		return (T) value;
	}

	protected String[] getParameterValues(final String name) {
		String[] value = request.getParameterValues(name);
		return value;
	}

	/**
	 * 等价于request.getSession().getAttribute(key).
	 */
	protected <T> T getSessionAttribute(final String key) {
		return (T) request.getSession().getAttribute(key);
	}

	protected PageRequest newPageRequest(final HttpServletRequest request,
			final String defaultSortColumns) {
		return PageRequestFactory.newPageRequest(request, defaultSortColumns);
	}

	protected PageRequest newPageRequest(final HttpServletRequest request,
			final String defaultSortColumns, int pageSize) {
		return PageRequestFactory.newPageRequest(request, defaultSortColumns, pageSize);
	}



	/**
	 * 等价于request.setAttribute(key, value).
	 */
	protected void put(final String key, final Object value) {
		request.setAttribute(key, value);
	}

	/**
	 * 将值设置到request中，参数为偶数，键值对对应request.setAttrubite(key, value);.
	 */
	protected void putValues(final Object... valeus) {
		if (valeus.length % 2 != 0) {
			log.debug("参数必须为偶数个");
			return;
		}
		for (int i = 0; i < valeus.length; i += 2) {
			put(valeus[i].toString(), valeus[i + 1]);
		}
	}

	/**
	 * 等价于request.getSession().removeAttribute(key).
	 */
	protected void removeSessionAttribute(final String key) {
		request.getSession().removeAttribute(key);
	}

	/**
	 * 保存错误消息在request中,messages.jsp会取出来显示此消息.
	 */
	protected void saveError(final String errorMsg) {
		if (StringUtils.isNotBlank(errorMsg)) {
			final List list = getOrCreateRequestAttribute("springErrors",
					ArrayList.class);
			list.add(errorMsg);
		}
	}

	protected void saveIntoModel(final Page<?> page,
			final PageRequest<?> pageRequest) {
		saveIntoModel("", page, pageRequest);
	}

	protected void saveIntoModel(final String tableId, final Page<?> page,
			final PageRequest<?> pageRequest) {
		Assert.notNull(tableId, "tableId must be not null");
		Assert.notNull(page, "page must be not null");

		put(tableId + "page", page);
		put(tableId + "totalRows", Integer.valueOf(page.getTotalCount()));
		put(tableId + "pageRequest", pageRequest);
	}

	protected void saveMessage(final String msg) {
		// DO NOTHING
	}
	
	/**
	 * 等价于request.getSession().setAttribute(key, value).
	 */
	protected void setSessionAttribute(final String key, final Object value) {
		request.getSession().setAttribute(key, value);
	}
	
	/**
	 * 重定向到操作成功页.
	 */
	public void success() {
		success(null);
	}
	
	/**
	 * 重定向到操作成功页.
	 * 
	 * @param msg
	 *            页面信息
	 */
	public void success(final String msg) {
		Result.success(msg, get("backUrl"), false);
	}

	/**
	 * 重定向到操作成功页.
	 * 
	 * @param msg
	 *            页面信息
	 */
	public void success(final String msg, final boolean needRrefeshTree) {
		Result.success(msg, get("backUrl"), needRrefeshTree);
	}
	
	/**
	 * 重定向到警告页
	 */
	public void warning(final String msg) {
		Result.warning(msg, get("backUrl"), false);
	}
	
}
