package com.njwd.rpc.monitor.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期工具类
 * 
 * @since version1.0
 */
public abstract class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd", java.util.Locale.CHINA);

	public static final DateFormat YEAR_MONTH_FORMAT = new SimpleDateFormat(
			"yyyyMM", java.util.Locale.CHINA);
	public static final DateFormat TIME_FORMAT = new SimpleDateFormat(
			"HH:mm:ss", java.util.Locale.CHINA);

	public static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", java.util.Locale.CHINA);

	/**
	 * 判断日期在两个日期之间
	 * @param date 需要判断的时间
	 * @param beginDate 开始区间时间
	 * @param endDate 结束区间时间
	 */
	public static boolean betweenDate(Date date ,Date beginDate,Date endDate){
		if(date.getTime()>=beginDate.getTime()&&date.getTime()<=endDate.getTime()){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 针对代理通道，如果起保时间为第二天。当前时间晚于17：30自动将时间修正为第三天零点。
	 * @return
	 */
	public static Date forceFixDate(Date startDate){
		Date now = new Date();
		if(isTomorrow(startDate,now)){
			//判断当前时间是否晚于17:30
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(now);
			calendar.set(Calendar.HOUR_OF_DAY, 17);
			calendar.set(Calendar.MINUTE,30);
			Date endTime = calendar.getTime();
			if(now.getTime()>endTime.getTime()){
				return addDays(truncate(now, Calendar.DATE),2);
			}
		}
		return startDate;
	}
	
	/**
	 * 判断给定的日期是否是某个时间的明天
	 * @param date
	 * @param compareDate
	 * @return
	 */
	public static boolean isTomorrow(Date date,Date compareDate){
		Date beginDate = addDays(compareDate, 1);
		beginDate = truncate(beginDate, Calendar.DATE);
		Date endDate = addDays(beginDate, 1);
		endDate.setTime(endDate.getTime() - 1);
		if(date.getTime()>=beginDate.getTime() && date.getTime()<=endDate.getTime()){
			return true;
		}
		return false;
	}
	/**
	 * DATE1>DATE2 返回1，DATE1=DATE2 返回0，DATE1<DATE2 返回-1
	 * 
	 * @param DATE1
	 * @param DATE2
	 * @return
	 */
	public static int compareDate(Date DATE1, Date DATE2) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1 = df.parse(df.format(DATE1));
			Date dt2 = df.parse(df.format(DATE2));
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	public static int compareDateTime(Date DATE1, Date DATE2) {

		try {
			if (DATE1.getTime() > DATE2.getTime()) {
				return 1;
			} else if (DATE1.getTime() < DATE2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获得本周第一天
	 */
	public static Date getWeekFirstDay() {
		Calendar c = Calendar.getInstance();
		int dayofweek = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayofweek == 0)
			dayofweek = 7;
		c.add(Calendar.DATE, -dayofweek + 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String s = sdf.format(c.getTime()) + " 00:00:00";
		return convertToDateTime(s);
	}

	/**
	 * 返回当前时间
	 * 
	 * @return 返回当前时间
	 */
	public static Date getCurrentDateTime() {
		java.util.Calendar calNow = java.util.Calendar.getInstance();
		java.util.Date dtNow = calNow.getTime();
		return dtNow;
	}

	/**
	 * 返回当前时间的数字 yyyyMMddHHmmss
	 * 
	 * @return 返回当前时间的数字
	 */
	public static String getCurrentTimeNumber() {
		return new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new java.util.Date());
	}

	/**
	 * 返回当前时间的数字 yyyyMMddHHmmssSSS
	 * 
	 * @return 返回当前时间的数字
	 */
	public static String getCurrentSTimeNumber() {
		return new SimpleDateFormat("yyyyMMddHHmmssSSS")
				.format(new java.util.Date());
	}

	/**
	 * 返回日期 yyyyMMdd
	 * 
	 * @return 返回当前时间的数字
	 */
	public static String getCurrentDTimeNumber() {
		return new SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
	}

	public static int getCurrentUnixTime() throws Exception {
		long epoch = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
				.parse("01/01/1970 00:00:00").getTime() / 1000;
		return Integer.parseInt(Long.toString(System.currentTimeMillis() / 1000
				- epoch));
	}

	/**
	 * @return 返回今天日期，时间部分为0。例如：2006-4-8 00:00:00
	 */
	public static Date getToday() {
		return truncate(new Date(), Calendar.DATE);
	}

	/**
	 * @return 返回今天日期，时间部分为23:59:59.999。例如：2006-4-19 23:59:59.999
	 */
	public static Date getTodayEnd() {
		return getDayEnd(new Date());
	}

	/**
	 * 将字符串转换为日期（不包括时间）
	 * 
	 * @param dateString
	 *            "yyyy-MM-dd"格式的日期字符串
	 * @return 日期
	 */
	public static Date convertToDate(String dateString) {
		try {
			return DATE_FORMAT.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 检查字符串是否为日期格式yyyy-MM-dd
	 * 
	 * @param dateString
	 * @return true=是；false=否
	 */
	public static boolean checkDateString(String dateString) {
		return (convertToDate(dateString) != null);
	}

	/**
	 * 将字符串转换为日期（包括时间）
	 * 
	 * @param dateString
	 *            "yyyy-MM-dd HH:mm:ss"格式的日期字符串
	 * @return 日期时间
	 */
	public static Date convertToDateTime(String dateTimeString) {
		try {
			return DATE_TIME_FORMAT.parse(dateTimeString);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 将字符串转换为日期（包括时间）
	 * 
	 * @param dateString
	 *            "dd/MM/yyyy HH:mm"格式的日期字符串
	 * @return 日期
	 */
	public static Date convertSimpleToDateTime(String dateString) {
		try {
			return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
					.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 检查字符串是否为日期时间格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dateString
	 * @return true=是；false=否
	 */
	public static boolean checkDateTimeString(String dateTimeString) {
		return (convertToDateTime(dateTimeString) != null);
	}

	/**
	 * 获取月底
	 * 
	 * @param year
	 *            年 4位年度
	 * @param month
	 *            月 1~12
	 * @return 月底的Date对象。例如：2006-3-31 23:59:59.999
	 */
	public static Date getMonthEnd(int year, int month) {
		StringBuffer sb = new StringBuffer(10);
		Date date;
		if (month < 12) {
			sb.append(Integer.toString(year));
			sb.append("-");
			sb.append(month + 1);
			sb.append("-1");
			date = convertToDate(sb.toString());
		} else {
			sb.append(Integer.toString(year + 1));
			sb.append("-1-1");
			date = convertToDate(sb.toString());
		}
		date.setTime(date.getTime() - 1);
		return date;
	}

	/**
	 * 获取当前日期的月底日期
	 * 
	 * @param when
	 *            要计算月底的日期
	 * @return 月底的Date对象。例如：2006-3-31 23:59:59.999
	 */
	public static Date getMonthEnd(Date when) {
		// Assert.notNull(when, "date must not be null !");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(when);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		return getMonthEnd(year, month);
	}

	/**
	 * 日期格式转换为字符串，结果：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String dateParseString(Date date) {
		if(date == null){
			return null;
		}
		java.text.DateFormat df = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}

	/**
	 * 日期格式转换为字符串 ，结果：yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String dateParseShortString(Date date) {
		java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}

	/**
	 * 获取给定日的最后一刻。
	 * 
	 * @param when
	 *            给定日
	 * @return 最后一刻。例如：2006-4-19 23:59:59.999
	 */
	public static Date getDayEnd(Date when) {
		Date date = truncate(when, Calendar.DATE);
		date = addDays(date, 1);
		date.setTime(date.getTime() - 1);
		return date;
	}

	/**
	 * 获取给定日的第一刻。
	 * 
	 * @param when
	 *            给定日
	 * @return 最后一刻。例如：2006-4-19 23:59:59.999
	 */
	public static Date getDay(Date when) {
		Date date = truncate(when, Calendar.DATE);
		date = addDays(date, -1);
		date.setTime(date.getTime() + 1);
		return date;
	}

	/**
	 * 日期加法
	 * 
	 * @param when
	 *            被计算的日期
	 * @param field
	 *            the time field. 在Calendar中定义的常数，例如Calendar.DATE
	 * @param amount
	 *            加数
	 * @return 计算后的日期
	 */
	public static Date add(Date when, int field, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(when);
		calendar.add(field, amount);
		return calendar.getTime();
	}

	/**
	 * 计算给定的日期加上给定的天数。
	 * 
	 * @param when
	 *            给定的日期
	 * @param amount
	 *            给定的天数
	 * @return 计算后的日期
	 */
	public static Date addDays(Date when, int amount) {
		if(amount ==0){
			return when;
		}
		return add(when, Calendar.DAY_OF_YEAR, amount);
	}

	/**
	 * 计算给定的日期加上给定的分钟数。
	 * 
	 * @param when
	 *            给定的日期
	 * @param amount
	 *            给定的分钟数
	 * @return 计算后的日期
	 */
	public static Date addMinutes(Date when, int amount) {

		return add(when, Calendar.MINUTE, amount);
	}

	/**
	 * 计算给定的日期加上给定的秒数。
	 * 
	 * @param when
	 *            给定的日期
	 * @param amount
	 *            给定的秒数
	 * @return 计算后的日期
	 */
	public static Date addSeconds(Date when, int amount) {

		return add(when, Calendar.SECOND, amount);
	}

	/**
	 * 计算给定的日期加上给定的月数。
	 * 
	 * @param when
	 *            给定的日期
	 * @param amount
	 *            给定的月数
	 * @return 计算后的日期
	 */
	public static Date addMonths(Date when, int amount) {
		return add(when, Calendar.MONTH, amount);
	}

	/**
	 * 将Date对象类型转化为日期(2006-09-17 05:20:05)的字符串
	 * 
	 * @param Date
	 * @return String
	 */
	public static String dateToString(Date date) {

		try {
			if (date != null)
				return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		} catch (Exception e) {
			e.printStackTrace();
			System.gc();
		}
		return null;
	}

	/**
	 * 将String 类型的转化为日期格式(2006-09-17 5:20:5)
	 * 
	 * @param String
	 * @return Date
	 */
	public static Date stringToDate(String dateStr) {

		try {
			if (dateStr != null)
				return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
			System.gc();
		}
		return null;
	}

	/**
	 * 字符串转为时间类型，格式：yyyy-MM-dd
	 * 
	 * @param dateStr
	 *            <String>
	 * @return Date
	 */
	public static final Date parseDate(String dateStr) {

		if (dateStr == null) {
			return null;
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		try {
			return df.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据指定格式转化日期
	 * 
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public static final Date parseDateByFormat(String dateStr, String format) {

		if (dateStr == null) {
			return null;
		}
		SimpleDateFormat df = new SimpleDateFormat(format);

		try {
			return df.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 得到具体时间如：几天前；几小时前；几分钟前；几秒钟前
	 * 
	 * @param time
	 *            传入一个Date类型的日期
	 * @return 根据当天当时当秒解析出距离当天当时当秒的字符串 String
	 */
	public static String getTimeInterval(Date time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			Long dateDiff = sdf.parse(sdf.format(new Date())).getTime()
					- sdf.parse(sdf.format(time)).getTime();

			Long day = dateDiff / (24 * 60 * 60 * 1000);

			if (day > 0) {
				return day + "天前";
			}

			Long hour = dateDiff / (60 * 60 * 1000);

			if (hour > 0) {
				return hour + "小时前";
			}

			Long minute = dateDiff / (60 * 1000);

			if (minute > 0) {
				return minute + "分钟前";
			}

			Long second = dateDiff / 1000;

			return second + "秒前";
		} catch (Exception e) {
			e.printStackTrace();
			return "未知";
		}
	}

	/**
	 * 由当前时间返回yyyy-mm-dd格式的日期字符串
	 * 
	 * @return String
	 */
	public static String getStringOfTodayDate() {

		Date d = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(d);
	}

	/**
	 * 比较两日期(字符)的大小,日期格式为yyMMdd.
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return boolean
	 */
	public static final boolean compareDate(String beginDate, String endDate) {

		long begin = Integer.parseInt(beginDate.substring(0, 4)
				+ beginDate.substring(5, 7) + beginDate.substring(8, 10));
		long end = Integer.parseInt(endDate.substring(0, 4)
				+ endDate.substring(5, 7) + endDate.substring(8, 10));
		if (begin > end) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 将Date对象类型转化为指定的格式字符串
	 * 
	 * @param date
	 *            <Date>日期
	 * @param format
	 *            <String>格式
	 * @return String
	 */
	public static String dateToString(Date date, String format) {

		try {
			if (date != null)
				return new SimpleDateFormat(format).format(date);
		} catch (Exception e) {
			e.printStackTrace();
			System.gc();
		}
		return null;
	}

	/**
	 * 系统日期减去传入日期得到天数
	 * 
	 * @param date1
	 *            传入日期
	 * @return 天数 long
	 */
	public static long sub(Date date1) {
		Date d2 = convertToDate(dateParseShortString(new Date()));
		Date d1 = convertToDate(dateParseShortString(date1));
		long day = (d2.getTime() - d1.getTime()) / (24 * 60 * 60 * 1000);
		return day;
	}

	/**
	 * 两个日期相减得到天数
	 * 
	 * @param date1
	 *            传入日期
	 * @param date2
	 * 
	 *            传入日期
	 * @return 天数 long
	 */
	public static int sub(Date date1, Date date2) {
		Date d1 = convertToDate(dateParseShortString(date1));
		Date d2 = convertToDate(dateParseShortString(date2));
		Long between = d2.getTime() - d1.getTime();
		if(between < 24 * 60 * 60 * 1000){
			return 1;
		}
		Long day = between / (24 * 60 * 60 * 1000);
		return day.intValue();
	}

	/**
	 * 日期相减得到年数
	 * 
	 * @param date1
	 *            相减的日期
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static long subDateGetYear(Date date1) {
		Date d2 = convertToDate(dateParseShortString(new Date()));
		Date d1 = convertToDate(dateParseShortString(date1));
		long day = (d2.getYear() - d1.getYear());
		return day + 1;
	}

	/**
	 * 获取当前天数
	 * 
	 * @return
	 */
	public static boolean getIsToday(Date oldDay) {
		int today = Integer.parseInt(new SimpleDateFormat("yyyyMMdd")
				.format(new Date()));
		int oldDays = Integer.parseInt(new SimpleDateFormat("yyyyMMdd")
				.format(oldDay));
		return today == oldDays;
	}

	
	public static int betweenMonth(Date d1,Date d2){
		 	Calendar c = Calendar.getInstance();
	        c.setTime(d1);
	        int year1 = c.get(Calendar.YEAR);
	        int month1 = c.get(Calendar.MONTH);
	         
	        c.setTime(d2);
	        int year2 = c.get(Calendar.YEAR);
	        int month2 = c.get(Calendar.MONTH);
	         
	        int result;
	        if(year1 == year2) {
	            result = month1 - month2;
	        } else {
	            result = 12*(year1 - year2) + month1 - month2;
	        }
	       return result;
	}
	/**
	 * 得到几天前的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateBefore(int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(new Date());
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}

	/**
	 * 计算下次结算日期 如果 date为2014-2-28日，addMonth为1，那下次日期为 2014-3-31 如果
	 * date为2014-1-29日，addMonth为1，那下次日期为 2014-2-28，addMonth为2，那下次日期为 2014-3-29
	 * 
	 * @param date
	 * @param addMonth
	 * @return
	 */
	public static Date calculateNextSettleDate(Date date, int addMonth) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		int maxOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		if (dayOfMonth == maxOfMonth) {
			calendar.add(Calendar.MONTH, addMonth);
			maxOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			calendar.set(Calendar.DAY_OF_MONTH, maxOfMonth);
		} else {
			calendar.add(Calendar.MONTH, addMonth);
		}
		return calendar.getTime();
	}

	/**
	 * 解析：指定日期字符串
	 * 
	 * @param date
	 *            java.util.Date
	 * @return long
	 * @exception 发生异常时
	 *                ,返回0
	 */
	public static Date parseStringAsDate(String strDate, String strFormat) {
		Date date = null;
		DateFormat dateFormat = new SimpleDateFormat(strFormat);
		try {
			date = dateFormat.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 判断时间是否在时间段内
	 * 
	 * @param date
	 *            当前时间 yyyy-MM-dd HH:mm:ss
	 * @param strDateBegin
	 *            开始时间 00:00:00
	 * @param strDateEnd
	 *            结束时间 00:05:00
	 * @return
	 */
	public static boolean isInDate(Date date, String strDateBegin,
			String strDateEnd) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String strDate = sdf.format(date);

		Date targetDate = parseStringAsDate(strDate, "HH:mm:ss");
		Date beginTime = parseStringAsDate(strDateBegin, "HH:mm:ss");
		Date endTime = parseStringAsDate(strDateEnd, "HH:mm:ss");

		if (targetDate.getTime() >= beginTime.getTime()
				&& targetDate.getTime() <= endTime.getTime()) {
			return true;
		}

		return false;
	}

	/**
	 * 根据用户生日计算年龄
	 */
	public static int getAgeByBirthday(Date birthday) {
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthday)) {
			return 0;
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime(birthday);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				age--;
			}
		}
		return age;
	}

	public static Date getMonthStart(){
		Calendar c = Calendar.getInstance();    
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
	}
	
	
	public static int calculateMonthIn(Date date1, Date date2) {
		Calendar cal1 = new GregorianCalendar();
		cal1.setTime(date1);
		Calendar cal2 = new GregorianCalendar();
		cal2.setTime(date2);
		int c = (cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR)) * 12
				+ cal1.get(Calendar.MONTH) - cal2.get(Calendar.MONTH);
		return c;
	}

	public static void main(String[] args) throws ParseException {

		// Date d = DateUtils.convertToDateTime("2014-1-30 14:53:25");
		// Date sd = DateUtils.calculateNextSettleDate(d, 1);
		// System.out.println(DateUtils.dateParseString(sd));
		// endTime:Fri Oct 31 23:59:59 CST 2200,authenticateDate:Wed Sep 30
		// 11:53:44 CST 2015,i:2,userId:645375EF26E8425B86C692C979E3CA2E

//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//		Calendar c1 = Calendar.getInstance();
//		Calendar c2 = Calendar.getInstance();
//
//		c1.setTime(sdf.parse("2016-11-3 14:47:00"));
//		c2.setTime(sdf.parse("2016-12-1 14:48:00"));
//
//		System.out.println(c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH));
		
		
	//	String info =DateUtils.dateToString(DateUtils.getToday(), "yyyy-MM-dd HH:mm:ss");
	//	System.out.println(info);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c1 = Calendar.getInstance();
		c1.setTime(sdf.parse("2017-1-8 14:47:00"));
		Date date = DateUtils.forceFixDate(c1.getTime());
		System.out.println(DateUtils.dateToString(date, "yyyy-MM-dd HH:mm:ss"));
//		Date d = DateUtils.convertToDateTime("2016-11-30 14:53:25");
//		Date d2 = DateUtils.convertToDateTime("2016-12-30 14:53:25");
//		System.out.println(DateUtils.sub(d, d2));
		
	}
}
