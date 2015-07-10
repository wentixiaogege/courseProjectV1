package edu.itu.course.dropwizard.resources;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public final class DateUtils {

	// some constant string
	public static final String FOMAT_DATE = "yyyy-MM-dd HH:mm:ss";
	public static final String FOMAT_DATE2 = "MM/dd/yyyy";
	public static final String FOMAT_DATE3 = "yyyy-MM-dd";
	
	static Calendar now;
	static Date today;

	static {
		// 取得当前的时间
		now = Calendar.getInstance();
		// 取得今天的日期，时间从0点开始算起。
		today = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE)).getTime();
	}

	/**
	 * 返回当前时间。
	 * 
	 * @return
	 */
	public static Calendar getNow() {
		return now;
	}

	public static long getNowLong() {
		return toUnixTime(now.getTime());
	}

	/**
	 * 变为Unix时间戳
	 * 
	 * @param cd
	 * @return
	 */
	public static long toUnixTime(Date dt) {

		return dt.getTime() / 1000;
	}

	/**
	 * 变为Unix时间戳
	 * 
	 * @param dtStr
	 *            ：必须是yyyy-MM-dd格式的字符串。
	 * @return
	 */
	public static long toUnixTime(String dtStr) {
		return toUnixTime(dtStr, FOMAT_DATE);
	}

	public static long toUnixTime(String dtStr, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		Date date;
		try {
			date = df.parse(String.valueOf(dtStr));
			return toUnixTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}

	}

	// /**
	// * 将具体的时间格式变为Unix时间戳
	// *
	// * @param dtStr
	// * ：必须是yyyy-MM-dd HH:mm:ss格式的字符串。
	// * @return
	// */
	// public static long toUnixTimeDetail(String dtStr) {
	// SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// Date date;
	// try {
	// date = df.parse(String.valueOf(dtStr));
	// return toUnixTime(date);
	// } catch (ParseException e) {
	// e.printStackTrace();
	// return -1;
	// }
	//
	// }

	/**
	 * 把Unix时间戳转化为java日期
	 * 
	 * @param uTime
	 * @return
	 */
	public static Date fromUnixTime(long uTime) {
		return new java.util.Date(uTime * 1000);
	}

	/**
	 * 把Unix时间戳转化为java日期，并格式化为yyyy-MM-dd HH:mm:ss的形式。
	 * 
	 * @param uTime
	 * @return
	 */
	public static String fromUnixTimeStr(long day) {
		return fromUnixTimeStr(day, FOMAT_DATE);
	}

	/**
	 * 把Unix时间戳转化为java日期，并格式化为yyyy-MM-dd HH:mm:ss的形式。
	 * 
	 * @param uTime
	 * @return
	 */
	public static String fromUnixTimeStr(long day, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(fromUnixTime(day));

	}

	/**
	 * 以Unixtime的形式返回今天日期
	 * 
	 * @return
	 */
	public static long getTodayLong() {
		return toUnixTime(today);
	}

	/**
	 * 以日期的形式返回今天日期
	 * 
	 * @return
	 */
	public static Date getToday() {
		return today;
	}

	/**
	 * 以字符串的形式返回今天日期
	 * 
	 * @return
	 */
	public static String getTodaystr() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(today);
	}

	/**
	 * 以yyMMdd字符串的形式返回今天日期
	 * 
	 * @return
	 */
	public static String getTodaySimplestr() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		return df.format(today);
	}

	public static String getBeforeDay(int n, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Calendar cal = now;
		cal.add(Calendar.DATE, 0 - n);
		return dateFormat.format(cal.getTime());
	}

	public static String getDateString(Date dat) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return getDateString(dat, df);
	}

	public static String getDateString(Date dat, DateFormat df) {
		// SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(dat);
	}

}
