package com.mytest.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: </p>
 * <p>@company YeePay </p>
 * @author 刘洋
 * @since 2011-3-1
 * @version 1.0
 */
public class DateUtils {
	private static Calendar c = Calendar.getInstance();
    private static GregorianCalendar cldr = new GregorianCalendar();

    /**
     * 返回系统当前的完整日期时间 <br>
     * 格式 1：2008-05-02 13:12:44 <br>
     * 格式 11：2008-05-02 <br>
     * 格式 2：2008/05/02 13:12:44 <br>
     * 格式 22：2008/05/02 <br>
     * 格式 3：2008年5月2日 13:12:44 <br>
     * 格式 33：2008年5月2日 <br>
     * 格式 4：2008年5月2日 13时12分44秒 <br>
     * 格式 5：2008年5月2日 星期五 13:12:44 <br>
     * 格式 6：2008年5月2日 星期五 13时12分44秒 <br>
     * @param 参数(formatType) :格式代码号
     * @return 字符串
     */
    public static String get(int formatType) {
        return get(formatType, new java.util.Date());
    }
    /**
     * 返回系统当前的完整日期时间 <br>
     * 格式 1：2008-05-02 13:12:44 <br>
     * 格式 11：2008-05-02 <br>
     * 格式 2：2008/05/02 13:12:44 <br>
     * 格式 22：2008/05/02 <br>
     * 格式 3：2008年5月2日 13:12:44 <br>
     * 格式 33：2008年5月2日 <br>
     * 格式 4：2008年5月2日 13时12分44秒 <br>
     * 格式 5：2008年5月2日 星期五 13:12:44 <br>
     * 格式 6：2008年5月2日 星期五 13时12分44秒 <br>
     * @param 参数(formatType) :格式代码号
     * @param date ：日期
     * @return 字符串
     */
    public static String get(int formatType, Date date) {
    	SimpleDateFormat sdf = null;
    	if (formatType == 1) {
    		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	}else if (formatType == 11) {
    		sdf = new SimpleDateFormat("yyyy-MM-dd");
    	} else if (formatType == 2) {
    		sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	} else if (formatType == 22) {
    		sdf = new SimpleDateFormat("yyyy/MM/dd");
    	} else if (formatType == 3) {
    		sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
    	} else if (formatType == 33) {
    		sdf = new SimpleDateFormat("yyyy年MM月dd日");
    	} else if (formatType == 4) {
    		sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
    	} else if (formatType == 5) {
    		sdf = new SimpleDateFormat("yyyy年MM月dd日 E HH:mm:ss");
    	} else if (formatType == 6) {
    		sdf = new SimpleDateFormat("yyyy年MM月dd日 E HH时mm分ss秒");
    	}
    	return sdf.format(date);
    }

    /**
	 * 格式化日期，返回字符串内容 例如："yyyy-MM-dd HH:mm:ss"
	 * @param d
	 * @param pattern
	 * @return
	 */
    public static String formatDate (Date d, String pattern){
        SimpleDateFormat s = new SimpleDateFormat(pattern);
        return s.format(d);
    }

	/**
	 * 获得日期的小时
	 * @param date
	 * @return
	 */
	public static int getHour(Date date){
		c.setTime(date);
		return c.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获得n天后是几号
	 * @param date
	 * @param n
	 * @return
	 */
	public static int getDay(Date date,int n){
		c.setTime(date);
		c.add(Calendar.DATE, n);
		return c.get(Calendar.DATE);
	}
	/**
	 * 获得n天后日期
	 * @param date
	 * @param n
	 * @param h(0-23)
	 * @param m(0-59)
	 * @param s(0-59)
	 * @param isStart(是否需要起始h,m,s。default：true)
	 * @return
	 */
	public static Date getDate(Date date, int n, Integer h,Integer m,Integer s, boolean isStart) {
		c.setTime(date);
		c.set(Calendar.YEAR, c.get(Calendar.YEAR));
		c.set(Calendar.MONTH, c.get(Calendar.MONTH));
		if(n != 0){
			c.add(Calendar.DATE, n);
		}else{
			c.set(Calendar.DATE, c.get(Calendar.DATE));
		}
		if(h != null){
			if(isStart){
				c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) + h);
			}else{
				c.set(Calendar.HOUR_OF_DAY, h);
			}
		}
		if(m != null){
			if(isStart){
				c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + m);
			}else{
				c.set(Calendar.MINUTE, m);
			}
		}
		if(s != null){
			if(isStart){
				c.set(Calendar.SECOND, c.get(Calendar.SECOND) + s);
			}else{
				c.set(Calendar.SECOND, s);
			}
		}
		return c.getTime();
	}
	/**
	 * 获得n天后日期(new Date())
	 * 有起始h,m,s
	 * @param n
	 * @param h(0-23)
	 * @param m(0-59)
	 * @param s(0-59)
	 * @return
	 */
	public static Date getDate(int n, Integer h,Integer m,Integer s) {
		return getDate(new Date(), n, h, m, s, true);
	}
	/**
	 * 获得n天后日期(new Date())
	 * @param n
	 * @param h(0-23)
	 * @param m(0-59)
	 * @param s(0-59)
	 * @param isStart(是否需要起始h,m,s。default：true)
	 * @return
	 */
	public static Date getDate(int n, Integer h,Integer m,Integer s,boolean isStart) {
		return getDate(new Date(), n, h, m, s, isStart);
	}
	
    /**
     * 获得指定日期在这个月的最后一天
     * @param date
     * @param datePattern
     * @return
     */
    public static String getLastDayOfMonth(String date, String datePattern)throws ParseException{

    	return DateUtils.getLastDayOfMonth(DateUtils.parseToDate(date, datePattern), datePattern);
    }

    public static String getLastDayOfMonth(Date date, String datePattern){
    	return DateUtils.formatDate(DateUtils.getLastDayOfMonth(date), datePattern);
    }

    public static Date getLastDayOfMonth(Date date){
    	Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, 1);
		int dom = c.get(Calendar.DAY_OF_MONTH);
		c.add(Calendar.DATE, -dom);

		return c.getTime();
    }

    /**
     * 获得指定日期在这个月的第一天
     * @param date
     * @param datePattern
     * @return
     */
    public static String getFirstDayOfMonth(String date, String datePattern)throws ParseException{

    	return DateUtils.getFirstDayOfMonth(DateUtils.parseToDate(date, datePattern), datePattern);
    }

    public static String getFirstDayOfMonth(Date date, String datePattern){

		return DateUtils.formatDate(DateUtils.getFirstDayOfMonth(date), datePattern);
    }

    public static Date getFirstDayOfMonth(Date date){
    	Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dom = c.get(Calendar.DAY_OF_MONTH);
		c.add(Calendar.DATE, -dom+1);

		return c.getTime();
    }

    /**
     * 清除日期的时间字段
     * @param dt
     */
    @SuppressWarnings("deprecation")
	static public void clearTime(Date dt) {
    	dt.setHours(0);
    	dt.setMinutes(0);
    	dt.setSeconds(0);
    }

    /**
     * 检查curDate是否在startDate和endDate内
     * @param curDate
     * @param startDate
     * @param endDate
     * @return
     */
    public static boolean isInDateRange (Date curDate, Date startDate, Date endDate){
        if (startDate == null || curDate == null){
            return false;
        }

        if (curDate.compareTo(startDate) >= 0){
            if (endDate == null){
                return true;
            } else if (curDate.compareTo(endDate) <= 0){
                return true;
            }
        }
        return false;
    }

    /**
     * 根据字符串日期格式（"YYYYMMDDhhmmss"）取得系统日期
     * @param sDate
     * @return
     */
    public static Date getDate (String sDate){
        int nYear = Integer.parseInt(sDate.substring(0, 4));
        int nMonth = Integer.parseInt(sDate.substring(4, 6));
        int nDay = Integer.parseInt(sDate.substring(6, 8));

        int nHour = 0;
        if (sDate.length() >= 10){
            nHour = Integer.parseInt(sDate.substring(8, 10));

        }
        int nMinute = 0;
        if (sDate.length() >= 12){
            nMinute = Integer.parseInt(sDate.substring(10, 12));

        }
        int nSecond = 0;
        if (sDate.length() >= 14){
            nSecond = Integer.parseInt(sDate.substring(12, 14));

        }
        cldr.set(nYear, nMonth - 1, nDay, nHour, nMinute, nSecond);
        return new Date(cldr.getTime().getTime());
    }



    public static String formatDate(Date d){
      SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
      return sf.format(d);
    }

    /**
     * 返回20051012形式的int
     * @param d Date
     * @return int
     */
    public static int getIntDate(Date d){
        return new Integer(formatDate(d,"yyyyMMdd")).intValue();
    }

    /**
     * 格式化日期，返回字符串内容
     * 例如："yyyy-MM-dd HH:mm:ss"
     * @param d
     * @param pattern
     * @return
     */
    public static String formatDate (Timestamp d, String pattern){
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        return sf.format(d);
    }

    /**
     * 字符串日期格式按照日期模式转换成日期
     * @param sDate   -- 字符串的日期
     * @param pattern -- 日期格式 （比如：yyyy-MM-dd）
     * @return
     * @throws ParseException
     */
    public static Date parseToDate (String sDate, String pattern) throws
        ParseException{
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        return sf.parse(sDate);
    }

    public static Date parseToDateWithyyyy_MM_dd (String sDate) throws
        ParseException{
        return parseToDate(sDate, "yyyy-MM-dd");
    }


    public static String parseToDateString (String sDate) throws
           ParseException{
           StringBuffer date = new StringBuffer(sDate);
          date.insert(4,'-');
          date.insert(7,'-');
          return date.toString();

       }

    public static String parseToDateString (int sDate) throws
        ParseException{
       return parseToDateString(sDate+"");

    }

    public static Timestamp parseToTimestampWithyyyy_MM_dd (String dateString) throws
        ParseException{
        return new Timestamp(parseToDateWithyyyy_MM_dd(dateString).getTime());
    }

    /**
     * 取当前时间
     */
    public static Date getCurrentDate (){
        return new Date();
    }

	public static Date getMinTime(Date dt) {
		Date dt1 = null;
		try {
			dt1 = DateUtils.parseToDate(DateUtils.formatDate(dt, "yyyyMMdd"), "yyyyMMdd");
		} catch (ParseException e) {
			System.out.println("date formate error ：" + dt + ".   " + e.getMessage());
		}
		return dt1;
	}

	public static int getDay(Date dt)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		return c.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 *
	 * @param dt
	 * @return
	 */
	public static int getMonth(Date dt) {
		if(dt == null){
			c.setTime(c.getTime());
		}else{
			c.setTime(dt);
		}
		return c.get(Calendar.MONTH) + 1;
	}

	public static int getIntMonth(Date dt)
	{
		return new Integer(formatDate(dt,"yyyyMM")).intValue();
	}

	/**
	 *
	 * @Title: 取得一个时间(date)向前/后推算 x秒(seconds)的时间
	 * @Description: 取得一个时间(date)向前/后推算 x秒(seconds)的时间
	 * @param Date date 参考时间
	 * @param Long seconds 推算秒数
	 * @return Date 返回推算后的时间
	 */
	public static Date getDayBeforeSeconds(Date date, Long seconds) {
		Date newDate = (Date) date.clone();
		newDate.setTime((date.getTime()/1000-seconds)*1000);
		return newDate;
	}

	public static Date getMonthDate(Date date, int n){
    	c.setTime(date);
    	c.add(Calendar.MONTH, n);
    	return c.getTime();
	}
	
    /**
     * 计算两个日期间隔的时间
     * 大于一天显示几天
     * 小于一天显示几小时
     * 小于一小时显示分钟
     * @param startDate
     * @param endDate
     * @return
     */
	public static Map<String, Long> getDaysBetween(Date startDate, Date endDate) {
		Map<String, Long> between = new HashMap<String, Long>();
		Long betweenSeconds = endDate.getTime() - startDate.getTime();
		if (Math.abs(betweenSeconds / (1000 * 60 * 60 * 24)) > 0) {
			between.put("D", betweenSeconds / (1000 * 60 * 60 * 24));
		} else if (Math.abs(betweenSeconds / (1000 * 60 * 60)) > 0) {
			between.put("H", betweenSeconds / (1000 * 60 * 60));
		} else if (Math.abs(betweenSeconds / (1000 * 60)) > 0) {
			between.put("M", betweenSeconds / (1000 * 60));
		} else {
			between.put("M", 0l);
		}
		return between;
	}
	
	/**
	 * 取当前系统时间，精确到秒
	 * 
	 * @return 系统时间
	 */
	public static int currentTimeSeconds() {
		return (int) (System.currentTimeMillis() / 1000);
	}
}