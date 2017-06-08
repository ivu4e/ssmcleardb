package com.weixiao.frame.base;

import javax.servlet.http.HttpServletRequest;

import com.weixiao.frame.page.PageRequest;

/**
 * 用于分页组件覆盖的类,新的分页组件覆盖此类的newPageRequest()方法以适合不同的分页创建.
 * 
 * @author badqiu
 */
public abstract class PageRequestFactory {

	private PageRequestFactory() {

	}

	public static PageRequest newPageRequest(final HttpServletRequest request, final String defaultSortColumns) {
		return SimpleTablePageRequestFactory.newPageRequest(request, defaultSortColumns);
	}

	public static PageRequest newPageRequest(final HttpServletRequest request, final String defaultSortColumns,
			final int defaultPageSize) {
		return SimpleTablePageRequestFactory.newPageRequest(request, defaultSortColumns, defaultPageSize);
	}

	/**
	 * jelee:增加skip跳过功能
	 * 
	 * @param request
	 * @param defaultSortColumns
	 * @param defaultPageSize
	 * @return
	 */
	public static PageRequest newPageRequest(final HttpServletRequest request, final String defaultSortColumns,
			final int defaultPageSize, final int skip) {
		return SimpleTablePageRequestFactory.newPageRequest(request, defaultSortColumns, defaultPageSize, skip);
	}

}
