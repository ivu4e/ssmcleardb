package com.weixiao.frame.base;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class AuthFilter implements Filter {

	@Override
	public void destroy() {
		// Filter销毁
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		String requestURI = req.getRequestURI().substring(req.getRequestURI().indexOf("/", 1),
				req.getRequestURI().length());
		System.out.println("requestURI=" + requestURI);
		
		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// 初始化方法
//		ServletContext sc = filterConfig.getServletContext();
//		XmlWebApplicationContext cxt = (XmlWebApplicationContext) WebApplicationContextUtils
//				.getWebApplicationContext(sc);

	}
}