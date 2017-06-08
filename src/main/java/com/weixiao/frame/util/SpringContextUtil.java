package com.weixiao.frame.util;

import java.util.Locale;

import javax.naming.AuthenticationException;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;

import com.weixiao.frame.base.ExceptionConstant;

/**
 * spring WebApplicationContext工具类
 * @author linjinliang
 *
 */
public class SpringContextUtil implements ApplicationContextAware{
	
	private static WebApplicationContext wac;

	public static void setApplicationContextStaticlly(WebApplicationContext webApplicationContext){
		wac = webApplicationContext;
	}
	
	public void setApplicationContext(ApplicationContext ac)
			throws BeansException {
		wac = (WebApplicationContext)ac;
	}
	
	public static ApplicationContext getApplicationContext(){
		return wac;
	}
	
	public static String getMessage(String key){
		return getApplicationContext().getMessage(key, null, null);
	}
	
	public static String getMessage(String key, Locale locale){
		if(locale == null){
			locale = new Locale("zh", "CN");
		}
		return getApplicationContext().getMessage(key, null, locale);
	}
	
	public static String getMessage(String key, Object[] args, Locale locale){
		if(locale == null){
			locale = new Locale("zh", "CN");
		}
		return getApplicationContext().getMessage(key, args, locale);
	}
	
	public static AuthenticationException getAuthenticationException(String key, String msg){
		key = getMessage(key);
		if(key == null)
			key = msg;
		return new AuthenticationException(key);
	}
	
	public static AuthenticationException getAuthenticationException(String key){
		key = getMessage(key);
		if(key == null)
			key = getMessage(ExceptionConstant.EXCEPTION_DEFAULT_INFO);
		return new AuthenticationException(key);
	}

}
