package com.weixiao.frame.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 移动相关操作实用类 MobileUtils
 * 
 * @author lipw
 * @date 2017年4月5日上午11:25:45
 */
public class MobileUtils {
	
	/**
	 * 验证手机号
	 * @author lipw
	 * @date   2017年4月5日上午11:34:07
	 * @param mobiles
	 * 手机号码
	 * @return
	 * 有效返回true,否则返回false
	 */
	public static boolean isMobileNO(String mobiles) {

		// Pattern p =
		// Pattern.compile("^((147)|(17[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4])|(18[0-9])|(17[0-9])|(147))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
}
