package com.weixiao.frame.util;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class DateUtils {
public static final String DEFAULT_DATE_FORMAT_PATTERN = "yyyy-MM-dd";
	
	public static final String NORMAL_DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	public static final String LENGTH_DATE_FORMAT_PATTERN = "yyyyMMddHHmmss";

	private static final Logger log = LoggerFactory.getLogger(DateUtils.class);
	
	private static final List<String> FULL_MONTH = Arrays.asList(
			new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"});
	
	private DateUtils() {		
	}	
	
	/**
	 * 获得当前年的最后一天.默认格式为"yyyy-MM-dd".
	 * @param year 据当前年的偏移量，如1,-3
	 * @return
	 */
	public static Date getLastDayOfYear(final int year) {
		return getLastDayOfYear(format(new Date()), year);
	}	
	
	/**
	 * 获得某年的最后一天.默认格式为"yyyy-MM-dd".
	 * @param dateStr 格式为"yyyy-MM-dd"
	 * @param year 据当前年的偏移量，如1,-3
	 * @return
	 */
	public static Date getLastDayOfYear(final String dateStr, final int year) {
		return getLastDayOfYear(dateStr, year, DEFAULT_DATE_FORMAT_PATTERN);
	}
	
	/**
	 * 获得某年的最后一天.使用指定的格式解析时间.
	 * @param dateStr 日期字符串
	 * @param year 据当前年的偏移量，如1,-3
	 * @return
	 */
	public static Date getLastDayOfYear(final String dateStr, final int year, final String pattern) {
		Date date = parseDate(dateStr, pattern);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.YEAR, (calendar.get(Calendar.YEAR) + year));
		calendar.set(Calendar.MONTH, 11);
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		return calendar.getTime();
	}
	
	/**
	 * 判断当前时间是否为最后一个月.
	 * @return
	 */
	public static boolean isLastMonth() {
		Calendar calendar = Calendar.getInstance();
		int currentMonth = calendar.get(Calendar.MONTH);
		return 11 == currentMonth;
	}
	
	/**
	 * 使用默认的格式"yyyy-MM-dd"，格式化日期.
	 * @param date 待格式化日期
	 * @return 格式化的日期
	 */
	public static String format(final Date date) {
		return format(date, DEFAULT_DATE_FORMAT_PATTERN);
	}
	
	/**
	 * 使用给定的格式，格式化日期.
	 * @param date 待格式化日期
	 * @param pattren 格式
	 * @return 格式化的日期
	 */
	public static String format(final Date date, final String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
	/**
	 * 使用默认格式解析日期.默认格式为"yyyy-MM-dd".
	 * @param date 待解析的日期
	 * @return 解析后的日期
	 */
	public static Date parseDate(final String date) {
		try {
			return org.apache.commons.lang.time.DateUtils.parseDate(date, new String[]{DEFAULT_DATE_FORMAT_PATTERN});
		} catch (ParseException e) {
			log.error("日期解析失败", e);
			return null;			
		}
	}
	
	/**
	 * 使用给定的格式解析日期.
	 * @param date 待解析的日期
	 * @param pattern 格式
	 * @return 解析后的日期
	 */
	public static Date parseDate(final String date, final String pattern) {
		try {
			return org.apache.commons.lang.time.DateUtils.parseDate(date, new String[]{pattern});
		} catch (ParseException e) {
			log.error("日期解析失败", e);
			return null;			
		}
	}
	
	/**
	 * 判断两个日期是否为同一年，使用默认格式"yyyy-MM-dd"解析日期.
	 * @param fromDate 起始时间
	 * @param toDate 结束时间
	 * @return
	 */
	public static boolean isSameYear(final String fromDate, final String toDate) {
		return isSameYear(fromDate, toDate, DEFAULT_DATE_FORMAT_PATTERN);
	}
	
	/**
	 * 判断两个日期是否为同一年，使用指定的格式解析时间.
	 * @param fromDate 起始时间
	 * @param toDate 结束时间
	 * @return
	 */
	public static boolean isSameYear(final String fromDate, final String toDate, final String pattern) {
		Calendar startCal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();
		startCal.setTime(DateUtils.parseDate(fromDate, pattern));
		endCal.setTime(DateUtils.parseDate(toDate, pattern));
		return startCal.get(Calendar.YEAR) == endCal.get(Calendar.YEAR);
	}
	
	/**
	 * 转变日期样式.
	 * @param date 待处理日期
	 * @param oldPattern 原有样式
	 * @param newPattern 新样式
	 * @return
	 */
	public static String convertPattern(final String date, final String oldPattern, final String newPattern) {
		return format(parseDate(date, oldPattern), newPattern);
	}
	
	public static List<String> getYears(int yearsCount) {
		Calendar cal = Calendar.getInstance();
		int nowYearInt = cal.get(Calendar.YEAR);
		List<String> years = new ArrayList<String>();
		for (int i = nowYearInt - yearsCount; i <= nowYearInt; i++) {
			years.add(i + "");
		}
		return years;
	}
	
	/**
	 * @param isNow
	 * @return
	 */
	public static List<String> getMonths(boolean isNow) {
		if (!isNow) {
			return FULL_MONTH;
		}
		
		Calendar cal = Calendar.getInstance();
		int nowMonthInt = cal.get(Calendar.MONTH) + 1;
		List<String> months = new ArrayList<String>();
		for (int i = 1; i <= nowMonthInt; i++) {
			months.add(autoAddPrefixZero(i, 2));
		}
		return months;
	}
	
	/**
	 * @param isNow
	 * @return
	 */
	public static List<String> getDays(int year, int month, boolean isNow) {
		Calendar cal = Calendar.getInstance();
		int nowDay = 31;
		if (isNow) {
			nowDay = cal.get(Calendar.DAY_OF_MONTH);
		}
		List<String> days = new ArrayList<String>();
		for (int i = 1; i <= nowDay; i ++) {
			cal.set(year, month, i);
			if (cal.get(Calendar.MONTH) == month) {
				days.add(autoAddPrefixZero(cal.get(Calendar.DAY_OF_MONTH), 2));
			}		
		}
		
		return days;
	}
	
	/**
	 * 数字不满位数自动补零.
	 * example autoAddPrefixZero(1, 4)
	 * 		   result: 0001
	 * @param num
	 * @param digit
	 * @return
	 */
	public static String autoAddPrefixZero(int num, Integer digit) {
		return String.format("%0" + digit + "d", num);
	}
	
	public static Integer getThisYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}
	
	/**
	 * 得到今天（年月日）: 
	 * @return
	 */
	public static Date getToday() {
		Calendar cale5 = Calendar.getInstance();
		cale5.set(Calendar.HOUR_OF_DAY, 0);
		cale5.set(Calendar.MINUTE, 0);
		cale5.set(Calendar.SECOND, 0);
		return cale5.getTime();
	}
	
	/**
	 * 得到今天（年月日）的字符串: 
	 * @return
	 */
	public static String getTodayString() {
		Calendar cale5 = Calendar.getInstance();
		cale5.set(Calendar.HOUR_OF_DAY, 0);
		cale5.set(Calendar.MINUTE, 0);
		cale5.set(Calendar.SECOND, 0);
		Date date= cale5.getTime();
		return format(date,DEFAULT_DATE_FORMAT_PATTERN);
	}
	
	/**
	 * 得到与今天相距指定天数的日期（年月日）: 
	 * @return
	 */
	public static Date getTheDay(int num) {
		Calendar cale5 = Calendar.getInstance();
		cale5.set(Calendar.HOUR_OF_DAY, 0);
		cale5.set(Calendar.MINUTE, 0);
		cale5.set(Calendar.SECOND, 0);
		cale5.add(Calendar.DAY_OF_YEAR, num);
		return cale5.getTime();
	}
	
	/**
	 * 得到与今天相距指定天数的日期（年月日）的字符串,时分秒设成当天最大时间: 
	 * @return
	 */
	public static String getTheDayMaxString(int num) {
		Calendar cale5 = Calendar.getInstance();
		cale5.set(Calendar.HOUR_OF_DAY, 23);
		cale5.set(Calendar.MINUTE, 59);
		cale5.set(Calendar.SECOND, 59);
		cale5.add(Calendar.DAY_OF_YEAR, num);
		return format(cale5.getTime(),NORMAL_DATE_FORMAT_PATTERN);
	}
	
	/**
	 * 得到与今天相距指定天数的日期（年月日）: 
	 * @return
	 */
	public static Date getTheDayTime(int num) {
		Calendar cale5 = Calendar.getInstance();
		cale5.add(Calendar.DAY_OF_YEAR, num);
		return cale5.getTime();
	}
	
	/**
	 * 得到某年后的今天（年月日）: 
	 * @return
	 */
	public static Date getTodayOfYears(int num) {
		Calendar cale = Calendar.getInstance();
		cale.set(Calendar.HOUR_OF_DAY, 0);
		cale.set(Calendar.MINUTE, 0);
		cale.set(Calendar.SECOND, 0);
		cale.add(Calendar.YEAR, num);
		return cale.getTime();
	}
	
	public static final String dayNames[] = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};

	
	
	
	 /**
	*  获取日期相差天数
	* @param 
	* @return 日期类型时间
	* @throws ParseException 
	*/
	 public static Long getDifferDays(String beginDate,String endDate) {
		 SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_DATE_FORMAT_PATTERN);
		 Long diffday=0l; //相差天数
		 try {
			 diffday = (formatter.parse(endDate).getTime()- formatter.parse(beginDate).getTime())/(1000*24*60*60);
		} catch (ParseException e) {
			//e.printStackTrace();
			log.error("日期解析失败.",e);
			diffday=null;
		}
		 return diffday;
	 }  
	 
	 
	 /**
		 * 根据一个日期，返回是星期几的字符串
		 * 
		 * @param sdate
		 * @return
		 */
	public static int getWeek(String sdate) {
		 // 再转换为时间
		 Date date = strToDate(sdate);
		 Calendar c = Calendar.getInstance();
		 c.setTime(date);
		 int hour=c.get(Calendar.DAY_OF_WEEK);
		 // hour中存的就是星期几了，其范围 1~7
		 // 1=星期日 7=星期六，其他类推
		 return hour;
	}
	
	
	/**
	 * 获取现在时间
	 * 
	 * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
	 */
	public static Date getNowDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		ParsePosition pos = new ParsePosition(8);
		Date currentTime_2 = formatter.parse(dateString, pos);
		return currentTime_2;
	}

	/**
	 * 获取现在时间
	 * 
	 * @return返回短时间格式 yyyy-MM-dd
	 */
	public static Date getNowDateShort() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		ParsePosition pos = new ParsePosition(8);
		Date currentTime_2 = formatter.parse(dateString, pos);
		return currentTime_2;
	}

	/**
	 * 获取现在时间
	 * 
	 * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
	 */
	public static String getStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 获取现在时间
	 * 
	 * @return 返回短时间字符串格式yyyy-MM-dd
	 */
	public static String getStringDateShort() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 获取时间 小时:分;秒 HH:mm:ss
	 * 
	 * @return
	 */
	public static String getTimeShort() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		Date currentTime = new Date();
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDateLong(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	/**
	 * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dateDate
	 * @return
	 */
	public static String dateToStrLong(java.util.Date dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	/**
	 * 将短时间格式时间转换为字符串 yyyy-MM-dd
	 * 
	 * @param dateDate
	 * @param k
	 * @return
	 */
	public static String dateToStr(java.util.Date dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	/**
	 * 将短时间格式字符串转换为时间 yyyy-MM-dd
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	/**
	 * 得到现在时间
	 * 
	 * @return
	 */
	public static Date getNow() {
		Date currentTime = new Date();
		return currentTime;
	}

	/**
	 * 提取一个月中的最后一天
	 * 
	 * @param day
	 * @return
	 */
	public static Date getLastDate(long day) {
		Date date = new Date();
		long date_3_hm = date.getTime() - 3600000 * 34 * day;
		Date date_3_hm_date = new Date(date_3_hm);
		return date_3_hm_date;
	}

	/**
	 * 得到现在时间
	 * 
	 * @return 字符串 yyyyMMdd HHmmss
	 */
	public static String getStringToday() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HHmmss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 得到现在小时
	 */
	public static String getHour() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		String hour;
		hour = dateString.substring(11, 13);
		return hour;
	}

	/**
	 * 得到现在分钟
	 * 
	 * @return
	 */
	public static String getTime() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		String min;
		min = dateString.substring(14, 16);
		return min;
	}
	
	/**
	 * 根据毫秒数获得当天的时间
	 * @param milliseconds
	 * @return
	 */
	public static String getDateInToday(long milliseconds){
		String startDate = getStringDateShort() + " 00:00:00";
		Timestamp temp = Timestamp.valueOf(startDate);
		Timestamp timestamp = new Timestamp(temp.getTime() + milliseconds);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {  
			return sdf.format(timestamp);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
		return "";
	}

	/**
	 * 根据用户传入的时间表示格式，返回当前时间的格式 如果是yyyyMMdd，注意字母y不能大写。
	 * 
	 * @param sformat
	 *            yyyyMMddhhmmss
	 * @return
	 */
	public static String getUserDate(String sformat) {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(sformat);
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 二个小时时间间的差值,必须保证二个时间都是"HH:MM"的格式，返回字符型的分钟
	 */
	public static String getTwoHour(String st1, String st2) {
		String[] kk = null;
		String[] jj = null;
		kk = st1.split(":");
		jj = st2.split(":");
		if (Integer.parseInt(kk[0]) < Integer.parseInt(jj[0]))
			return "0";
		else {
			double y = Double.parseDouble(kk[0]) + Double.parseDouble(kk[1])
					/ 60;
			double u = Double.parseDouble(jj[0]) + Double.parseDouble(jj[1])
					/ 60;
			if ((y - u) > 0)
				return y - u + "";
			else
				return "0";
		}
	}

	/**
	 * 得到二个日期间的间隔天数
	 */
	public static String getTwoDay(String sj1, String sj2) {
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		long day = 0;
		try {
			java.util.Date date = myFormatter.parse(sj1);
			java.util.Date mydate = myFormatter.parse(sj2);
			day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		} catch (Exception e) {
			return "";
		}
		return day + "";
	}

	/**
	 * 时间前推或后推分钟,其中JJ表示分钟.
	 */
	public static String getPreTime(String sj1, String jj) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String mydate1 = "";
		try {
			Date date1 = format.parse(sj1);
			long Time = (date1.getTime() / 1000) + Integer.parseInt(jj) * 60;
			date1.setTime(Time * 1000);
			mydate1 = format.format(date1);
		} catch (Exception e) {
		}
		return mydate1;
	}

	/**
	 * 得到一个时间延后或前移几天的时间,nowdate为时间,delay为前移或后延的天数
	 */
	public static String getNextDay(String nowdate, String delay) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String mdate = "";
			Date d = strToDate(nowdate);
			long myTime = (d.getTime() / 1000) + Integer.parseInt(delay) * 24
					* 60 * 60;
			d.setTime(myTime * 1000);
			mdate = format.format(d);
			return mdate;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 判断是否润年
	 * 
	 * @param ddate
	 * @return
	 */
	public static boolean isLeapYear(String ddate) {

		/**
		 * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
		 * 3.能被4整除同时能被100整除则不是闰年
		 */
		Date d = strToDate(ddate);
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(d);
		int year = gc.get(Calendar.YEAR);
		if ((year % 400) == 0)
			return true;
		else if ((year % 4) == 0) {
			if ((year % 100) == 0)
				return false;
			else
				return true;
		} else
			return false;
	}

	/**
	 * 返回美国时间格式 26 Apr 2006
	 * 
	 * @param str
	 * @return
	 */
	public static String getEDate(String str) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(str, pos);
		String j = strtodate.toString();
		String[] k = j.split(" ");
		return k[2] + k[1].toUpperCase() + k[5].substring(2, 4);
	}
	
	/**
	 * 获取当前日期所在周
	 * @return
	 */
	public static int getWeekNumber(){ 
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTime(new Date());
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 获取一个月的最后一天
	 * 
	 * @param dat
	 * @return
	 */
	public static String getEndDateOfMonth(String dat) {// yyyy-MM-dd
		String str = dat.substring(0, 8);
		String month = dat.substring(5, 7);
		int mon = Integer.parseInt(month);
		if (mon == 1 || mon == 3 || mon == 5 || mon == 7 || mon == 8
				|| mon == 10 || mon == 12) {
			str += "31";
		} else if (mon == 4 || mon == 6 || mon == 9 || mon == 11) {
			str += "30";
		} else {
			if (isLeapYear(dat)) {
				str += "29";
			} else {
				str += "28";
			}
		}
		return str;
	}

	/**
	 * 判断二个时间是否在同一个周
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameWeekDates(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
		if (0 == subYear) {
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
					.get(Calendar.WEEK_OF_YEAR))
				return true;
		} else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
			// 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
					.get(Calendar.WEEK_OF_YEAR))
				return true;
		} else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
					.get(Calendar.WEEK_OF_YEAR))
				return true;
		}
		return false;
	}

	/**
	 * 产生周序列,即得到当前时间所在的年度是第几周
	 * 
	 * @return
	 */
	public static String getSeqWeek() {
		Calendar c = Calendar.getInstance(Locale.CHINA);
		String week = Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
		if (week.length() == 1)
			week = "0" + week;
		String year = Integer.toString(c.get(Calendar.YEAR));
		return year + week;
	}

	/**
	 * 获得一个日期所在的周的星期几的日期，如要找出2002年2月3日所在周的星期一是几号
	 * 
	 * @param sdate
	 * @param num
	 * @return
	 */
	public static String getWeek(String sdate, String num) {
		// 再转换为时间
		Date dd = strToDate(sdate);
		Calendar c = Calendar.getInstance();
		c.setTime(dd);
		if (num.equals("1")) // 返回星期一所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		else if (num.equals("2")) // 返回星期二所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		else if (num.equals("3")) // 返回星期三所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		else if (num.equals("4")) // 返回星期四所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		else if (num.equals("5")) // 返回星期五所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		else if (num.equals("6")) // 返回星期六所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		else if (num.equals("0")) // 返回星期日所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
	}

	/**
	 * 两个时间之间的天数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long getDays(String date1, String date2) {
		if (date1 == null || date1.equals(""))
			return 0;
		if (date2 == null || date2.equals(""))
			return 0;
		// 转换为标准时间
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = null;
		java.util.Date mydate = null;
		try {
			date = myFormatter.parse(date1);
			mydate = myFormatter.parse(date2);
		} catch (Exception e) {
		}
		long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		return day;
	}

	/**
	 * 形成如下的日历 ， 根据传入的一个时间返回一个结构 星期日 星期一 星期二 星期三 星期四 星期五 星期六 下面是当月的各个时间
	 * 此函数返回该日历第一行星期日所在的日期
	 * 
	 * @param sdate
	 * @return
	 */
	public static String getNowMonth(String sdate) {
		// 取该时间所在月的一号
		sdate = sdate.substring(0, 8) + "01";

		// 得到这个月的1号是星期几
		Date date = strToDate(sdate);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int u = c.get(Calendar.DAY_OF_WEEK);
		String newday = getNextDay(sdate, (1 - u) + "");
		return newday;
	}

	/**
	 * 取得数据库主键 生成格式为yyyymmddhhmmss+k位随机数
	 * 
	 * @param k
	 *            表示是取几位随机数，可以自己定
	 */

	public static String getNo(int k) {

		return getUserDate("yyyyMMddhhmmss") + getRandom(k);
	}

	/**
	 * 返回一个随机数
	 * 
	 * @param i
	 * @return
	 */
	public static String getRandom(int i) {
		Random jjj = new Random();
		// int suiJiShu = jjj.nextInt(9);
		if (i == 0)
			return "";
		String jj = "";
		for (int k = 0; k < i; k++) {
			jj = jj + jjj.nextInt(9);
		}
		return jj;
	}

	/**
	 * 判断是否为有效的日期字符串
	 * @param args
	 */
	public static boolean RightDate(String date) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		if (date == null)
			return false;
		if (date.length() > 10) {
			sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		} else {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		}
		try {
			sdf.parse(date);
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断两个日期是否为同一天
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameDay(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		return isSameDay(cal1, cal2);
	}

	/**
	 * <p>
	 * Checks if two calendar objects are on the same day ignoring time.
	 * </p>
	 *
	 * <p>
	 * 28 Mar 2002 13:45 and 28 Mar 2002 06:01 would return true. 28 Mar 2002
	 * 13:45 and 12 Mar 2002 13:45 would return false.
	 * </p>
	 * 
	 * @param cal1
	 *            the first calendar, not altered, not null
	 * @param cal2
	 *            the second calendar, not altered, not null
	 * @return true if they represent the same day
	 * @throws IllegalArgumentException
	 *             if either calendar is <code>null</code>
	 * @since 2.1
	 */
	public static boolean isSameDay(Calendar cal1, Calendar cal2) {
		if (cal1 == null || cal2 == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
				&& cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1
					.get(Calendar.DAY_OF_YEAR) == cal2
				.get(Calendar.DAY_OF_YEAR));
	}
	
	  //-----------------------------------------------------------------------
    /**
     * Adds a number of milliseconds to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addMilliseconds(Date date, int amount) {
        return add(date, Calendar.MILLISECOND, amount);
    }
    
    /**
     * Adds to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param calendarField  the calendar field to add to
     * @param amount  the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException if the date is null
     * @deprecated Will become privately scoped in 3.0
     */
    public static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

    public static void main(String[] args) {
		System.out.println(DateUtils.getTodayString());
	}
}
