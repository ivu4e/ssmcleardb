package com.weixiao.frame.base;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.WebUtils;

import com.weixiao.frame.page.PageRequest;

/**
 * 分页请求Factory.
 * 
 * @author xuqiao
 * 
 */
public abstract class SimpleTablePageRequestFactory {
	private SimpleTablePageRequestFactory() {

	}

	private static final int MAX_PAGE_SIZE = 500;
	private static final int DEFAULT_PAGE_SIZE = 10;
	static {
		System.out.println("SimpleTablePageRequestFactory.DEFAULT_PAGE_SIZE=" + DEFAULT_PAGE_SIZE);
		System.out.println("SimpleTablePageRequestFactory.MAX_PAGE_SIZE=" + MAX_PAGE_SIZE);
	}

	public static PageRequest<Map> newPageRequest(final HttpServletRequest request, final String defaultSortColumns) {
		return newPageRequest(request, defaultSortColumns, DEFAULT_PAGE_SIZE);
	}

	public static PageRequest<Map> newPageRequest(final HttpServletRequest request, final String defaultSortColumns,
			final int defaultPageSize) {
		return newPageRequest(request, defaultSortColumns, defaultPageSize, 0);
	}

	public static PageRequest<Map> bindPageRequestParameters(PageRequest<Map> pageRequest, HttpServletRequest request,
			String defaultSortColumns, int defaultPageSize, int skip) {
		pageRequest.setPageNumber(getIntParameter(request, "pageNumber", 1));
		pageRequest.setPageSize(getIntParameter(request, "pageSize", defaultPageSize));
		pageRequest.setSortColumns(getStringParameter(request, "sortColumns", defaultSortColumns));
		pageRequest.setFilters(WebUtils.getParametersStartingWith(request, "s_"));
		pageRequest.setSkip(skip);

		if (pageRequest.getPageSize() > MAX_PAGE_SIZE) {
			pageRequest.setPageSize(MAX_PAGE_SIZE);
		}
		return pageRequest;
	}

	static String getStringParameter(final HttpServletRequest request, final String paramName, final String defaultValue) {
		final String value = request.getParameter(paramName);
		return StringUtils.isEmpty(value) ? defaultValue : value;
	}

	static int getIntParameter(final HttpServletRequest request, final String paramName, final int defaultValue) {
		final String value = request.getParameter(paramName);
		return StringUtils.isEmpty(value) ? defaultValue : Integer.parseInt(value);
	}

	/**
	 * @param request
	 * @param defaultSortColumns
	 * @param defaultPageSize
	 * @param skip
	 * @return
	 */
	public static PageRequest newPageRequest(HttpServletRequest request, String defaultSortColumns,
			int defaultPageSize, int skip) {

		final PageRequest<Map> pageRequest = new PageRequest();
		return bindPageRequestParameters(pageRequest, request, defaultSortColumns, defaultPageSize, skip);

	}

}
