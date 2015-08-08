package com.smart.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * 时间转换工具
 * 
 * @author gaowenming
 * @newDate 2011-3-23
 */
public class DateUtil {
	public final static SimpleDateFormat g_SimpleDateFormat = new SimpleDateFormat("yyyyMMdd");
	public final static SimpleDateFormat g_SimpleDateFormat_I = new SimpleDateFormat("yyyy-MM-dd");
	public final static SimpleDateFormat g_SimpleDateFormat_II = new SimpleDateFormat("yyyyMM");
	public final static SimpleDateFormat sdfDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public final static SimpleDateFormat sdfDateTimeFormat_I = new SimpleDateFormat("yyyyMMddHHmmss");
	public final static SimpleDateFormat sdfDateTimeFormat_IIII = new SimpleDateFormat("HH:mm:ss");
	public final static SimpleDateFormat sdfDateTimeFormat_YYYY = new SimpleDateFormat("yyyy");

	/**
	 * 获取系统当前日期
	 * 
	 * @return
	 */
	public static Date getCurrentDate() {
		return new Date();
	}

	/**
	 * 返回日期格式(yyyy-MM-dd HH:mm:ss)
	 * 
	 * @param date
	 * @return
	 */
	public static String toDateTimeStr(Date date) {
		if (date == null) {
			return "";
		}
		return sdfDateTimeFormat.format(date);
	}

	/**
	 * 返回日期格式(yyyyMMddHHmmss)
	 * 
	 * @param date
	 * @return
	 */
	public static String toDateTimeStr2(Date date) {
		if (date == null) {
			return "";
		}
		return sdfDateTimeFormat_I.format(date);
	}

	/**
	 * 
	 * 描述 : <描述函数实现的功能>. <br>
	 * <p>
	 * <使用方法说明>
	 * </p>
	 * 
	 * @return
	 */
	public static String getCurrentDateStr() {
		return sdfDateTimeFormat_I.format(new Date());
	}

	/**
	 * 返回日期格式(yyyy-MM-dd)
	 * 
	 * @param date
	 * @return
	 */
	public static String toDateStr(Date date) {
		if (date == null) {
			return "";
		}
		return g_SimpleDateFormat_I.format(date);
	}

	/**
	 * 返回时间格式(HH:mm:ss)
	 * 
	 * @param date
	 * @return
	 */
	public static String toTimeStr(Date date) {
		if (date == null) {
			return "";
		}
		return sdfDateTimeFormat_IIII.format(date);
	}

	/**
	 * 返回日期格式(yyyyMMdd)
	 * 
	 * @param date
	 * @return
	 */
	public static String toDateStr2(Date date) {
		if (date == null) {
			return "";
		}
		return g_SimpleDateFormat.format(date);
	}

	/**
	 * 返回日期格式(yyyyMM)
	 * 
	 * @param date
	 * @return
	 */
	public static String toDateStr3(Date date) {
		if (date == null) {
			return "";
		}
		return g_SimpleDateFormat_II.format(date);
	}

	/**
	 * <p>
	 * 得到xxxx年xx月xx日 日期格式。
	 * </p>
	 * 
	 * @param date
	 * @return
	 */
	public static String toChinaDateStr(Date date) {
		if (date == null) {
			return "";
		}
		// 得到yyyy-mm-dd格式日期格式
		String dateStr = toDateStr(date);
		StringBuffer sb = new StringBuffer();
		if (dateStr != null && dateStr.length() > 0) {
			String[] newStr = dateStr.split("-");
			// 得到月
			int month = Integer.valueOf(newStr[1]);
			// 得到日
			int day = Integer.valueOf(newStr[2]);
			sb.append(newStr[0]).append("年");
			sb.append(month).append("月").append(day).append("日");
		}
		return sb.toString();
	}

	/**
	 * <p>
	 * 获取当前系统时间的小时数
	 * </p>
	 * 
	 * @return
	 */
	public static int getCurrentHour() {
		Calendar calendar = new GregorianCalendar();
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * <p>
	 * 获取当前系统时间的分钟数
	 * </p>
	 * 
	 * @return
	 */
	public static int getCurrentMinutes() {
		Calendar calendar = new GregorianCalendar();
		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * <p>
	 * 获取本月第一天日期（格式如YYYYMMDD）,如果当前日为当月1日,则返回上月第一日
	 * </p>
	 * 
	 * @return
	 */
	public static String getMonthFirstDay() {
		Calendar calendar = new GregorianCalendar();
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = 0;
		if (day == 1) {
			calendar.add(Calendar.MONTH, -1);
		}
		month = calendar.get(Calendar.MONTH);
		if (month < 10) {
			return "" + calendar.get(Calendar.YEAR) + "0" + (month + 1) + "01";
		} else {
			return "" + calendar.get(Calendar.YEAR) + month + "01";
		}
	}

	public static Date getFristDayOfMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		return calendar.getTime();
	}

	public static Date getLastDayOfMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		return calendar.getTime();
	}

	/**
	 * 获取当前时间前几天或后几天的日期
	 * 
	 * @return
	 */
	public static Date getAddDays(int days) {
		Calendar calendar = new GregorianCalendar();
		calendar.add(Calendar.DAY_OF_YEAR, days);
		return calendar.getTime();
	}

	/**
	 * 获取某个月后的日期格式（yyyyMMdd）
	 * 
	 * @return
	 */
	public static String getAfterMonth(int monthNum) {
		Calendar calendar = new GregorianCalendar();
		calendar.add(Calendar.MONTH, monthNum);
		return g_SimpleDateFormat.format(calendar.getTime());
	}

	/**
	 * 返回日期（格式yyyyMMdd）
	 * 
	 * @param timeMillis
	 * @return
	 */
	public static String getFormatDate(long timeMillis) {
		return sdfDateTimeFormat_I.format(new Date(timeMillis));
	}

	public static String getPredate() {
		Date nowDate = new Date();
		String nowdates = g_SimpleDateFormat_I.format(nowDate);
		String[] dates = nowdates.split("-");
		int year = Integer.parseInt(dates[0]);
		int month = Integer.parseInt(dates[1]);
		int day = Integer.parseInt(dates[2]) - 1;
		if (day == 0) {
			switch (month - 1) {
			case 1:
				day = 31;
				break;
			case 3:
				day = 31;
				break;
			case 5:
				day = 31;
				break;
			case 7:
				day = 31;
				break;
			case 8:
				day = 31;
				break;
			case 10:
				day = 31;
				break;
			case 0:
				month = 13;
				year = year - 1;
				day = 31;
				break;
			case 4:
				day = 30;
				break;
			case 6:
				day = 30;
				break;
			case 9:
				day = 30;
				break;
			case 11:
				day = 30;
				break;
			case 2:
				if (year % 4 == 0) {
					day = 29;
				} else {
					day = 28;
				}
				break;
			default:
				break;
			}
			month = month - 1;
		}
		String predate = Integer.toString(year) + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
		return predate;
	}

	public static long getCurrentYear() {
		return Long.parseLong(sdfDateTimeFormat_YYYY.format(new Date()));
	}

	/**
	 * 判断一个日期字符串是否合法
	 * 
	 * @param date
	 * @param format
	 * @return
	 * @author liufengyu
	 */
	public static boolean isDateStringCorrect(String date, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);

		try {
			df.setLenient(false);
			df.parse(date);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * 将字符串类型的日期格式 转换为 符合要求的日期格式
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getStrDate4String(String date, String format) {
		if (date == null || date.trim().equals("")) {
			return "";
		} else {
			SimpleDateFormat df = new SimpleDateFormat(format);
			try {
				Date d = df.parse(date);
				return df.format(d);
			} catch (ParseException e) {
				return "";
			}
		}
	}

	/**
	 * 将Date类型的日期格式 转换为 符合要求的 String日期格式
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String toDateStr(Date date, String format) {
		if (date == null) {
			return "";
		} else {
			SimpleDateFormat df = new SimpleDateFormat(format);
			return df.format(date);
		}
	}

	/**
	 * 将字符串类型的日期格式 转换为 符合要求的 Date类型的日期格式
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static Date strToDate(String date, String format) {
		if (date == null || date.trim().equals("")) {
			return null;
		} else {
			SimpleDateFormat df = new SimpleDateFormat(format);
			try {
				return df.parse(date);
			} catch (ParseException e) {
				return null;
			}
		}
	}

	/**
	 * 计算指定日期时间之间的时间差
	 * 
	 * @param beginStr
	 *            开始日期字符串
	 * @param endStr
	 *            结束日期字符串
	 * @param f
	 *            时间差的形式0-秒,1-分种,2-小时,3--天 日期时间字符串格式:yyyyMMddHHmmss
	 * */
	public static int getInterval(String beginStr, String endStr, int f) {
		int hours = 0;
		try {
			Date beginDate = sdfDateTimeFormat.parse(beginStr);
			Date endDate = sdfDateTimeFormat.parse(endStr);
			long millisecond = endDate.getTime() - beginDate.getTime(); // 日期相减得到日期差X(单位:毫秒)
			/**
			 * Math.abs((int)(millisecond/1000)); 绝对值 1秒 = 1000毫秒
			 * millisecond/1000 --> 秒 millisecond/1000*60 - > 分钟
			 * millisecond/(1000*60*60) -- > 小时 millisecond/(1000*60*60*24) -->
			 * 天
			 * */
			switch (f) {
			case 0: // second
				return (int) (millisecond / 1000);
			case 1: // minute
				return (int) (millisecond / (1000 * 60));
			case 2: // hour
				return (int) (millisecond / (1000 * 60 * 60));
			case 3: // day
				return (int) (millisecond / (1000 * 60 * 60 * 24));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hours;
	}

	/**
	 * 得到起始日期前或后天数的日期
	 * 
	 * @param starttime
	 *            起始日期 格式：yyyy-MM-dd
	 * @param days
	 * @return
	 * @throws ParseException
	 */
	public static Date getStartDateInterval(String starttime, int days) {
		// 格式化起始时间 yyyyMMdd
		Date startDate = null;
		try {
			startDate = g_SimpleDateFormat_I.parse(starttime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar startTime = Calendar.getInstance();
		startTime.clear();
		startTime.setTime(startDate);

		startTime.add(Calendar.DAY_OF_YEAR, days);
		return startTime.getTime();
	}

	/**
	 * 得到起始日期和结束日期之间的天数
	 * 
	 * @param beginStr
	 *            起始日期
	 * @param endStr
	 *            结束日期
	 * @param format
	 *            根据 日期参数的格式，传对应的SimpleDateFormat格式
	 * @return 天数
	 */
	public static int getDaysInterval(String beginStr, String endStr, SimpleDateFormat format) {

		try {
			Date beginDate = format.parse(beginStr);
			Date endDate = format.parse(endStr);
			long millisecond = endDate.getTime() - beginDate.getTime(); // 日期相减得到日期差X(单位:毫秒)
			return (int) (millisecond / (1000 * 60 * 60 * 24));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static final String DATE_FORMAT_FULL = "yyyy-MM-dd HH:mm:ss";

	public static final String DATE_FORMAT_SHORT = "yyyy-MM-dd";

	public static final String DATE_FORMAT_COMPACT = "yyyyMMdd";

	public static final String DATE_FORMAT_COMPACTFULL = "yyyyMMddHHmmss";

	public static final String DATE_FORMAT_FULL_MSEL = "yyyyMMddHHmmssSSSS";

	public static final String DATE_YEAR_MONTH = "yyyyMM";

	/**
	 * 得到当前日期
	 * 
	 * @return String 当前日期 yyyy-MM-dd HH:mm:ss格式
	 * @author kevin
	 */
	public static String getCurDateTime() {
		Calendar now = Calendar.getInstance(TimeZone.getDefault());
		// String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
		DateFormat sdf = new SimpleDateFormat(DATE_FORMAT_FULL);
		sdf.setTimeZone(TimeZone.getDefault());
		return (sdf.format(now.getTime()));
	}

	/**
	 * 得到当前日期YYYYMM格式
	 * 
	 * @return String 当前日期 yyyyMM格式
	 * @author kevin
	 */
	public static String getCurDateYYYYMM() {
		Calendar now = Calendar.getInstance(TimeZone.getDefault());

		DateFormat sdf = new SimpleDateFormat(DATE_YEAR_MONTH);
		sdf.setTimeZone(TimeZone.getDefault());
		return (sdf.format(now.getTime()));
	}

	/**
	 * 得到下个月日期YYYYMM格式
	 * 
	 * @return String 当前日期 yyyyMM格式
	 * @author kevin
	 */
	public static String getNextMonthDateYYYYMM() {
		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		cal.add(Calendar.MONTH, 1);
		DateFormat sdf = new SimpleDateFormat(DATE_YEAR_MONTH);
		sdf.setTimeZone(TimeZone.getDefault());
		return (sdf.format(cal.getTime()));
	}

	/**
	 * 得到当前日期
	 * 
	 * @return String 当前日期 yyyyMMdd格式
	 * @author kevin
	 */
	public static String getCurDateYYYYMMDD() {
		Calendar now = Calendar.getInstance(TimeZone.getDefault());

		DateFormat sdf = new SimpleDateFormat(DATE_FORMAT_COMPACT);
		sdf.setTimeZone(TimeZone.getDefault());
		return (sdf.format(now.getTime()));
	}

	/**
	 * 是否是今天
	 * 
	 * @param strDate
	 *            yyyy-MM-dd
	 * @return
	 */
	public static boolean isCurrentDay(String strDate) {
		boolean bRet = false;
		try {
			Calendar now = Calendar.getInstance(TimeZone.getDefault());
			// String DATE_FORMAT = "yyyy-MM-dd";
			DateFormat sdf = new SimpleDateFormat(DATE_FORMAT_SHORT);
			sdf.setTimeZone(TimeZone.getDefault());
			Date date1 = sdf.parse(strDate);
			String strDate1 = sdf.format(date1);
			String strDate2 = sdf.format(now.getTime());
			bRet = strDate1.equalsIgnoreCase(strDate2);
		} catch (ParseException e) {
			e.printStackTrace();

		}
		return bRet;
	}

	/**
	 * 是否是今天
	 * 
	 * @param strDate
	 *            yyyy-MM-dd
	 * @return
	 */
	public static boolean isCurrentDay(Date dt) {
		boolean bRet = false;
		Calendar now = Calendar.getInstance(TimeZone.getDefault());

		DateFormat sdf = new SimpleDateFormat(DATE_FORMAT_SHORT);
		sdf.setTimeZone(TimeZone.getDefault());
		String strDate1 = sdf.format(dt);
		String strDate2 = sdf.format(now.getTime());
		bRet = strDate1.equalsIgnoreCase(strDate2);
		return bRet;
	}

	/**
	 * 获取几小时后的时间
	 * 
	 * @param hour
	 *            小时
	 * @param format
	 *            hh:mm:ss
	 * @return HH:MM:SS
	 */
	public static String getAfterDateTime(int hour, String format) {
		long lTime = new Date().getTime() + hour * 60 * 60 * 1000;
		Calendar calendar = new GregorianCalendar();
		java.util.Date date_now = new java.util.Date(lTime);
		calendar.setTime(date_now);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		java.util.Date date = calendar.getTime();
		return sdf.format(date);
	}

	/**
	 * 得到当前日期
	 * 
	 * @param format日期格式
	 * @return String 当前日期 format日期格式
	 * @author kevin
	 */
	public static String getCurDateTime(String format) {
		try {
			Calendar now = Calendar.getInstance(TimeZone.getDefault());
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			sdf.setTimeZone(TimeZone.getDefault());
			return (sdf.format(now.getTime()));
		} catch (Exception e) {
			return getCurDateTime(); // 如果无法转化，则返回默认格式的时间。
		}
	}

	/**
	 * 得到时间戳
	 * 
	 * @param null
	 * @return String 当前日期时间戳(yyyyMMddHHmmssSSSS)
	 * @author kevin
	 */
	public static String getTimeStamp() {
		try {
			Calendar now = Calendar.getInstance(TimeZone.getDefault());
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_FULL_MSEL);
			sdf.setTimeZone(TimeZone.getDefault());
			return (sdf.format(now.getTime()));
		} catch (Exception e) {
			return getCurDateTime(); // 如果无法转化，则返回默认格式的时间。
		}
	}

	/**
	 * 日期转字符串
	 * 
	 * @return String
	 * @author kevin
	 */
	public static String parseDateToString(Date thedate, String format) {
		DateFormat df = new SimpleDateFormat(format);
		if (thedate != null) {
			return df.format(thedate.getTime());
		}
		return null;
	}

	// parseDateToString(Date thedate, String format)的重载方法
	public static String parseDateToString(Date thedate) {
		// String format = "yyyy-MM-dd";
		return parseDateToString(thedate, DATE_FORMAT_SHORT);
	}

	/**
	 * 字符串转日期
	 * 
	 * @return Date
	 * @author kevin
	 */
	public static Date parseStringToDate(String thedate, String format) {
		DateFormat sdf = new SimpleDateFormat(format);
		Date dd1 = null;
		try {
			dd1 = sdf.parse(thedate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dd1;
	}

	/**
	 * 判断日期是否符合要求
	 */
	public static boolean parseDateStr(String thedate, String format) {
		DateFormat sdf = new SimpleDateFormat(format);
		Date dd1 = null;
		try {
			dd1 = sdf.parse(thedate);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	/**
	 * 由String型日期转成format形式String
	 * 
	 * @param format1原先格式
	 * @param format2转化格式
	 * @return String
	 * @author kevin
	 */
	public static String changeFormatDateString(String format1, String format2, String strDate) {
		if (strDate == null)
			return "";
		if (strDate.length() >= format1.length() && format1.length() >= format2.length()) {
			return parseDateToString(parseStringToDate(strDate, format1), format2);
		}
		return strDate;
	}

	/**
	 * 得到N天后的日期
	 * 
	 * @param theDate某日期
	 *            格式 yyyy-MM-dd
	 * @param nDayNum
	 *            N天
	 * @return String N天后的日期
	 * @author kevin
	 */
	public static String afterNDaysDate(String theDate, String nDayNum, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date dd1 = sdf.parse(theDate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dd1);
			cal.add(Calendar.DAY_OF_YEAR, Integer.parseInt(nDayNum));
			sdf.setTimeZone(TimeZone.getDefault());
			return (sdf.format(cal.getTime()));
		} catch (Exception e) {
			return getCurDateTime(format); // 如果无法转化，则返回默认格式的时间。
		}
	}

	/**
	 * 得到N小时后的日期
	 * 
	 * @param theDate某日期
	 *            格式传入传出格式都是 format格式
	 * @param nDayNum
	 *            N小时
	 * @return String N小时后的日期
	 * @author kevin
	 */
	public static String afterNHoursDate(String theDate, String nHourNum, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date dd1 = sdf.parse(theDate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dd1);
			cal.add(Calendar.HOUR_OF_DAY, Integer.parseInt(nHourNum));
			sdf.setTimeZone(TimeZone.getDefault());
			return (sdf.format(cal.getTime()));
		} catch (Exception e) {
			return getCurDateTime(format); // 如果无法转化，则返回默认格式的时间。
		}
	}

	/**
	 * 得到N分钟后的日期
	 * 
	 * @param theDate某日期
	 *            格式 yyyy-MM-dd
	 * @param nDayNum
	 *            N分钟
	 * @return String N分钟后的日期
	 * @author kevin
	 */
	public static String afterNMinsDate(String theDate, String nMinNum, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date dd1 = sdf.parse(theDate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dd1);
			cal.add(Calendar.MINUTE, Integer.parseInt(nMinNum));
			sdf.setTimeZone(TimeZone.getDefault());
			return (sdf.format(cal.getTime()));
		} catch (Exception e) {
			return getCurDateTime(format); // 如果无法转化，则返回默认格式的时间。
		}
	}

	/**
	 * 得到N秒后的日期
	 * 
	 * @param theDate某日期
	 *            格式 yyyy-MM-dd
	 * @param nDayNum
	 *            N分钟
	 * @return String N分钟后的日期
	 * @author kevin
	 */
	public static String afterNSecondsDate(String theDate, String nMinNum, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date dd1 = sdf.parse(theDate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dd1);
			cal.add(Calendar.MILLISECOND, Integer.parseInt(nMinNum));
			sdf.setTimeZone(TimeZone.getDefault());
			return (sdf.format(cal.getTime()));
		} catch (Exception e) {
			return getCurDateTime(format); // 如果无法转化，则返回默认格式的时间。
		}
	}

	// 比较两个字符串格式日期大小,带格式的日期
	public static boolean isBefore(String strdat1, String strdat2, String format) {
		try {
			Date dat1 = parseStringToDate(strdat1, format);
			Date dat2 = parseStringToDate(strdat2, format);
			return dat1.before(dat2);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// 比较两个字符串格式日期大小,带格式的日期,返回int
	public static long isBefore_int(String strdat1, String strdat2, String format) {
		long result = 0;
		try {
			Date dat1 = parseStringToDate(strdat1, format);
			Date dat2 = parseStringToDate(strdat2, format);
			return dat2.getTime() - dat1.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 比较两个字符串格式日期大小,默认转换格式:yyyy-MM-dd
	public static boolean isBefore(String strdat1, String strdat2) {
		// String format = "yyyy-MM-dd";
		return isBefore(strdat1, strdat2, DATE_FORMAT_SHORT);
	}

	/**
	 * 获取休息时间
	 * 
	 * @param strTime
	 *            strTime=" 3:00:00"; 需要休息的时间，注意有空格
	 * @return 需要休息的时间
	 * @throws ParseException
	 */
	public static long getSleepTime(String strTime) throws ParseException {
		long p = 1;
		long l_date = System.currentTimeMillis();
		java.util.Date date_now = new java.util.Date(l_date);
		SimpleDateFormat fmt = new SimpleDateFormat(DATE_FORMAT_SHORT);
		String strDate = fmt.format(date_now) + strTime;
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_FULL);
		if (date_now.before(df.parse(strDate)))
			p = (df.parse(strDate)).getTime() - l_date;
		else {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date_now);
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			java.util.Date date = calendar.getTime();
			strDate = fmt.format(date) + strTime;
			p = (df.parse(strDate)).getTime() - l_date;
		}
		return p;
	}

	public static Timestamp getCurrentTime() {
		return new Timestamp(System.currentTimeMillis());
	}

	public static String getHourMinute(String fullTime) {

		return fullTime.substring(11, 16);
	}

	// 得到日期数组中最小日期
	public static String getMinDateOfArray(String dateArray[]) {
		String dateTmp = "";
		if (dateArray != null) {
			dateTmp = dateArray[0];
			for (int i = 1; i < dateArray.length; i++) {
				if (isBefore(dateArray[i], dateTmp, DATE_FORMAT_SHORT)) {
					dateTmp = dateArray[i];
				}
			}
		}
		return dateTmp;
	}

	// 得到日期数组中最大日期
	public static String getMaxDateOfArray(String dateArray[]) {
		String dateTmp = "";
		if (dateArray != null) {
			dateTmp = dateArray[0];
			for (int i = 1; i < dateArray.length; i++) {
				if (isBefore(dateTmp, dateArray[i], DATE_FORMAT_SHORT)) {
					dateTmp = dateArray[i];
				}
			}
		}
		return dateTmp;
	}

	public static boolean hasNextDayInArray(String dateArray[], String dateTmp) {
		int index = 0;
		if (dateArray != null) {
			Arrays.sort(dateArray);
			for (int i = 0; i < dateArray.length; i++) {
				if (dateArray[i].equals(dateTmp)) {
					index = i;
				}
			}
			// System.out.println(index);
			return index + 1 != dateArray.length;
		}
		return false;
	}

	public static boolean hasPreviousDayInArray(String dateArray[], String dateTmp) {
		int index = 0;
		if (dateArray != null) {
			Arrays.sort(dateArray);
			for (int i = 0; i < dateArray.length; i++) {
				if (dateArray[i].equals(dateTmp)) {
					index = i;
				}
			}
			return index != 0;
		}
		return false;
	}

	/*
	 * 得到上一个月或者下一个月的日期
	 */
	public static String getDayafterMonth(String theDate, int month, String formatStr) {
		Calendar now = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		Date dat1 = parseStringToDate(theDate, formatStr);
		now.setTime(dat1);
		sdf.setTimeZone(TimeZone.getDefault());
		now.add(Calendar.MONTH, month);
		return sdf.format(now.getTime());
	}

}
