package com.pi.client.pi_client.utlis.date;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {
  public static int x;                  // 日期属性：年
  public static int y;                  // 日期属性：月
  public static int z;                  // 日期属性：日
  private final static Logger logger = LoggerFactory.getLogger(DateUtil.class);
  private static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<>();

  private static final Object object = new Object();

  /**
   * 获取SimpleDateFormat
   *
   * @param pattern 日期格式
   * @return SimpleDateFormat对象
   * @throws RuntimeException 异常：非法日期格式
   */
  private static SimpleDateFormat getDateFormat(String pattern) throws RuntimeException {
    SimpleDateFormat dateFormat = threadLocal.get();
    if (dateFormat == null) {
      synchronized (object) {
        if (dateFormat == null) {
          dateFormat = new SimpleDateFormat(pattern);
          dateFormat.setLenient(false);
          threadLocal.set(dateFormat);
        }
      }
    }
    dateFormat.applyPattern(pattern);
    return dateFormat;
  }

  /**
   * 获取日期中的某数值。如获取月份
   *
   * @param date     日期
   * @param dateType 日期格式
   * @return 数值
   */
  private static int getInteger(Date date, int dateType) {
    int num = 0;
    Calendar calendar = Calendar.getInstance();
    if (date != null) {
      calendar.setTime(date);
      num = calendar.get(dateType);
    }
    return num;
  }

  /**
   * 增加日期中某类型的某数值。如增加日期
   *
   * @param date     日期字符串
   * @param dateType 类型
   * @param amount   数值
   * @return 计算后日期字符串
   */
  private static String addInteger(String date, int dateType, int amount) {
    String dateString = null;
    DateStyle dateStyle = getDateStyle(date);
    if (dateStyle != null) {
      Date myDate = StringToDate(date, dateStyle);
      myDate = addInteger(myDate, dateType, amount);
      dateString = DateToString(myDate, dateStyle);
    }
    return dateString;
  }

  /**
   * 增加日期中某类型的某数值。如增加日期
   *
   * @param date     日期
   * @param dateType 类型
   * @param amount   数值
   * @return 计算后日期
   */
  private static Date addInteger(Date date, int dateType, int amount) {
    Date myDate = null;
    if (date != null) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      calendar.add(dateType, amount);
      myDate = calendar.getTime();
    }
    return myDate;
  }

  /**
   * 获取精确的日期
   *
   * @param timestamps 时间long集合
   * @return 日期
   */
  private static Date getAccurateDate(List<Long> timestamps) {
    Date date = null;
    long timestamp = 0;
    Map<Long, long[]> map = new HashMap<>();
    List<Long> absoluteValues = new ArrayList<>();

    if (timestamps != null && timestamps.size() > 0) {
      if (timestamps.size() > 1) {
        for (int i = 0; i < timestamps.size(); i++) {
          for (int j = i + 1; j < timestamps.size(); j++) {
            long absoluteValue = Math.abs(timestamps.get(i) - timestamps.get(j));
            absoluteValues.add(absoluteValue);
            long[] timestampTmp = {timestamps.get(i), timestamps.get(j)};
            map.put(absoluteValue, timestampTmp);
          }
        }

        // 有可能有相等的情况。如2012-11和2012-11-01。时间戳是相等的。此时minAbsoluteValue为0
        // 因此不能将minAbsoluteValue取默认值0
        long minAbsoluteValue = -1;
        if (!absoluteValues.isEmpty()) {
          minAbsoluteValue = absoluteValues.get(0);
          for (int i = 1; i < absoluteValues.size(); i++) {
            if (minAbsoluteValue > absoluteValues.get(i)) {
              minAbsoluteValue = absoluteValues.get(i);
            }
          }
        }

        if (minAbsoluteValue != -1) {
          long[] timestampsLastTmp = map.get(minAbsoluteValue);

          long dateOne = timestampsLastTmp[0];
          long dateTwo = timestampsLastTmp[1];
          if (absoluteValues.size() > 1) {
            timestamp = Math.abs(dateOne) > Math.abs(dateTwo) ? dateOne : dateTwo;
          }
        }
      } else {
        timestamp = timestamps.get(0);
      }
    }

    if (timestamp != 0) {
      date = new Date(timestamp);
    }
    return date;
  }

  /**
   * 判断字符串是否为日期字符串
   *
   * @param date 日期字符串
   * @return true or false
   */
  public static boolean isDate(String date) {
    boolean isDate = false;
    if (date != null) {
      if (getDateStyle(date) != null) {
        isDate = true;
      }
    }
    return isDate;
  }

  /**
   * 获取日期字符串的日期风格。失敗返回null。
   *
   * @param date 日期字符串
   * @return 日期风格
   */
  public static DateStyle getDateStyle(String date) {
    DateStyle dateStyle = null;
    Map<Long, DateStyle> map = new HashMap<>();
    List<Long> timestamps = new ArrayList<>();
    for (DateStyle style : DateStyle.values()) {
      if (style.isShowOnly()) {
        continue;
      }
      Date dateTmp = null;
      if (date != null) {
        try {
          ParsePosition pos = new ParsePosition(0);
          dateTmp = getDateFormat(style.getValue()).parse(date, pos);
          if (pos.getIndex() != date.length()) {
            dateTmp = null;
          }
        } catch (Exception e) {
          logger.error("date parsing error", e);
        }
      }
      if (dateTmp != null) {
        timestamps.add(dateTmp.getTime());
        map.put(dateTmp.getTime(), style);
      }
    }
    Date accurateDate = getAccurateDate(timestamps);
    if (accurateDate != null) {
      dateStyle = map.get(accurateDate.getTime());
    }
    return dateStyle;
  }

  /**
   * 将日期字符串转化为日期。失败返回null。
   *
   * @param date 日期字符串
   * @return 日期
   */
  public static Date StringToDate(String date) {
    Date myDate = null;
    if (date != null && !date.trim().equals("")) {
      DateStyle dateStyle = getDateStyle(date);
      return StringToDate(date, dateStyle);
    }
    return myDate;
  }

  /**
   * 将日期字符串转化为日期。失败返回null。
   *
   * @param date    日期字符串
   * @param pattern 日期格式
   * @return 日期
   */
  public static Date StringToDate(String date, String pattern) {
    Date myDate = null;
    if (date != null) {
      try {
        myDate = getDateFormat(pattern).parse(date);
      } catch (Exception e) {
        logger.error("date parsing error", e);
      }
    }
    return myDate;
  }

  /**
   * 将日期字符串转化为日期。失败返回null。
   *
   * @param date      日期字符串
   * @param dateStyle 日期风格
   * @return 日期
   */
  public static Date StringToDate(String date, DateStyle dateStyle) {
    Date myDate = null;
    if (dateStyle != null) {
      myDate = StringToDate(date, dateStyle.getValue());
    }
    return myDate;
  }

  /**
   * 将日期转化为日期字符串。失败返回null。
   *
   * @param date    日期
   * @param pattern 日期格式
   * @return 日期字符串
   */
  public static String DateToString(Date date, String pattern) {
    String dateString = null;
    if (date != null) {
      try {
        dateString = getDateFormat(pattern).format(date);
      } catch (Exception e) {
        logger.error("date formating error", e);
      }
    }
    return dateString;
  }

  /**
   * 将日期转化为日期字符串。失败返回null。
   *
   * @param date      日期
   * @param dateStyle 日期风格
   * @return 日期字符串
   */
  public static String DateToString(Date date, DateStyle dateStyle) {
    String dateString = null;
    if (dateStyle != null) {
      dateString = DateToString(date, dateStyle.getValue());
    }
    return dateString;
  }

  /**
   * 将日期字符串转化为另一日期字符串。失败返回null。
   *
   * @param date       旧日期字符串
   * @param newPattern 新日期格式
   * @return 新日期字符串
   */
  public static String StringToString(String date, String newPattern) {
    DateStyle oldDateStyle = getDateStyle(date);
    return StringToString(date, oldDateStyle, newPattern);
  }

  /**
   * 将日期字符串转化为另一日期字符串。失败返回null。
   *
   * @param date         旧日期字符串
   * @param newDateStyle 新日期风格
   * @return 新日期字符串
   */
  public static String StringToString(String date, DateStyle newDateStyle) {
    DateStyle oldDateStyle = getDateStyle(date);
    return StringToString(date, oldDateStyle, newDateStyle);
  }

  /**
   * 将日期字符串转化为另一日期字符串。失败返回null。
   *
   * @param date        旧日期字符串
   * @param olddPattern 旧日期格式
   * @param newPattern  新日期格式
   * @return 新日期字符串
   */
  public static String StringToString(String date, String olddPattern, String newPattern) {
    return DateToString(StringToDate(date, olddPattern), newPattern);
  }

  /**
   * 将日期字符串转化为另一日期字符串。失败返回null。
   *
   * @param date         旧日期字符串
   * @param olddDteStyle 旧日期风格
   * @param newParttern  新日期格式
   * @return 新日期字符串
   */
  public static String StringToString(String date, DateStyle olddDteStyle, String newParttern) {
    String dateString = null;
    if (olddDteStyle != null) {
      dateString = StringToString(date, olddDteStyle.getValue(), newParttern);
    }
    return dateString;
  }

  /**
   * 将日期字符串转化为另一日期字符串。失败返回null。
   *
   * @param date         旧日期字符串
   * @param olddPattern  旧日期格式
   * @param newDateStyle 新日期风格
   * @return 新日期字符串
   */
  public static String StringToString(String date, String olddPattern, DateStyle newDateStyle) {
    String dateString = null;
    if (newDateStyle != null) {
      dateString = StringToString(date, olddPattern, newDateStyle.getValue());
    }
    return dateString;
  }

  /**
   * 将日期字符串转化为另一日期字符串。失败返回null。
   *
   * @param date         旧日期字符串
   * @param olddDteStyle 旧日期风格
   * @param newDateStyle 新日期风格
   * @return 新日期字符串
   */
  public static String StringToString(String date, DateStyle olddDteStyle, DateStyle newDateStyle) {
    String dateString = null;
    if (olddDteStyle != null && newDateStyle != null) {
      dateString = StringToString(date, olddDteStyle.getValue(), newDateStyle.getValue());
    }
    return dateString;
  }

  /**
   * 增加日期的年份。失败返回null。
   *
   * @param date       日期
   * @param yearAmount 增加数量。可为负数
   * @return 增加年份后的日期字符串
   */
  public static String addYear(String date, int yearAmount) {
    return addInteger(date, Calendar.YEAR, yearAmount);
  }

  /**
   * 增加日期的年份。失败返回null。
   *
   * @param date       日期
   * @param yearAmount 增加数量。可为负数
   * @return 增加年份后的日期
   */
  public static Date addYear(Date date, int yearAmount) {
    return addInteger(date, Calendar.YEAR, yearAmount);
  }

  /**
   * 增加日期的月份。失败返回null。
   *
   * @param date        日期
   * @param monthAmount 增加数量。可为负数
   * @return 增加月份后的日期字符串
   */
  public static String addMonth(String date, int monthAmount) {
    return addInteger(date, Calendar.MONTH, monthAmount);
  }

  /**
   * 增加日期的月份。失败返回null。
   *
   * @param date        日期
   * @param monthAmount 增加数量。可为负数
   * @return 增加月份后的日期
   */
  public static Date addMonth(Date date, int monthAmount) {
    return addInteger(date, Calendar.MONTH, monthAmount);
  }

  /**
   * 增加日期的天数。失败返回null。
   *
   * @param date      日期字符串
   * @param dayAmount 增加数量。可为负数
   * @return 增加天数后的日期字符串
   */
  public static String addDay(String date, int dayAmount) {
    return addInteger(date, Calendar.DATE, dayAmount);
  }

  /**
   * 增加日期的天数。失败返回null。
   *
   * @param date      日期
   * @param dayAmount 增加数量。可为负数
   * @return 增加天数后的日期
   */
  public static Date addDay(Date date, int dayAmount) {
    return addInteger(date, Calendar.DATE, dayAmount);
  }

  /**
   * 增加日期的小时。失败返回null。
   *
   * @param date       日期字符串
   * @param hourAmount 增加数量。可为负数
   * @return 增加小时后的日期字符串
   */
  public static String addHour(String date, int hourAmount) {
    return addInteger(date, Calendar.HOUR_OF_DAY, hourAmount);
  }

  /**
   * 增加日期的小时。失败返回null。
   *
   * @param date       日期
   * @param hourAmount 增加数量。可为负数
   * @return 增加小时后的日期
   */
  public static Date addHour(Date date, int hourAmount) {
    return addInteger(date, Calendar.HOUR_OF_DAY, hourAmount);
  }

  /**
   * 增加日期的分钟。失败返回null。
   *
   * @param date         日期字符串
   * @param minuteAmount 增加数量。可为负数
   * @return 增加分钟后的日期字符串
   */
  public static String addMinute(String date, int minuteAmount) {
    return addInteger(date, Calendar.MINUTE, minuteAmount);
  }

  /**
   * 增加日期的分钟。失败返回null。
   *
   * @param date         日期
   * @param minuteAmount 增加数量。可为负数
   * @return 增加分钟后的日期
   */
  public static Date addMinute(Date date, int minuteAmount) {
    return addInteger(date, Calendar.MINUTE, minuteAmount);
  }

  /**
   * 增加日期的秒钟。失败返回null。
   *
   * @param date         日期字符串
   * @param secondAmount 增加数量。可为负数
   * @return 增加秒钟后的日期字符串
   */
  public static String addSecond(String date, int secondAmount) {
    return addInteger(date, Calendar.SECOND, secondAmount);
  }

  /**
   * 增加日期的秒钟。失败返回null。
   *
   * @param date         日期
   * @param secondAmount 增加数量。可为负数
   * @return 增加秒钟后的日期
   */
  public static Date addSecond(Date date, int secondAmount) {
    return addInteger(date, Calendar.SECOND, secondAmount);
  }

  /**
   * 获取日期的年份。失败返回0。
   *
   * @param date 日期字符串
   * @return 年份
   */
  public static int getYear(String date) {
    return getYear(StringToDate(date));
  }

  /**
   * 获取日期的年份。失败返回0。
   *
   * @param date 日期
   * @return 年份
   */
  public static int getYear(Date date) {
    return getInteger(date, Calendar.YEAR);
  }

  /**
   * 获取日期的月份。失败返回0。
   *
   * @param date 日期字符串
   * @return 月份
   */
  public static int getMonth(String date) {
    return getMonth(StringToDate(date));
  }

  /**
   * 获取日期的月份。失败返回0。
   *
   * @param date 日期
   * @return 月份
   */
  public static int getMonth(Date date) {
    return getInteger(date, Calendar.MONTH) + 1;
  }

  /**
   * 获取日期的天数。失败返回0。
   *
   * @param date 日期字符串
   * @return 天
   */
  public static int getDay(String date) {
    return getDay(StringToDate(date));
  }

  /**
   * 获取日期的天数。失败返回0。
   *
   * @param date 日期
   * @return 天
   */
  public static int getDay(Date date) {
    return getInteger(date, Calendar.DATE);
  }

  /**
   * 获取日期的小时。失败返回0。
   *
   * @param date 日期字符串
   * @return 小时
   */
  public static int getHour(String date) {
    return getHour(StringToDate(date));
  }

  /**
   * 获取日期的小时。失败返回0。
   *
   * @param date 日期
   * @return 小时
   */
  public static int getHour(Date date) {
    return getInteger(date, Calendar.HOUR_OF_DAY);
  }

  /**
   * 获取日期的分钟。失败返回0。
   *
   * @param date 日期字符串
   * @return 分钟
   */
  public static int getMinute(String date) {
    return getMinute(StringToDate(date));
  }

  /**
   * 获取日期的分钟。失败返回0。
   *
   * @param date 日期
   * @return 分钟
   */
  public static int getMinute(Date date) {
    return getInteger(date, Calendar.MINUTE);
  }

  /**
   * 获取日期的秒钟。失败返回0。
   *
   * @param date 日期字符串
   * @return 秒钟
   */
  public static int getSecond(String date) {
    return getSecond(StringToDate(date));
  }

  /**
   * 获取日期的秒钟。失败返回0。
   *
   * @param date 日期
   * @return 秒钟
   */
  public static int getSecond(Date date) {
    return getInteger(date, Calendar.SECOND);
  }

  /**
   * 获取日期 。默认yyyy-MM-dd格式。失败返回null。
   *
   * @param date 日期字符串
   * @return 日期
   */
  public static String getDate(String date) {
    return StringToString(date, DateStyle.YYYY_MM_DD);
  }

  /**
   * 获取日期。默认yyyy-MM-dd格式。失败返回null。
   *
   * @param date 日期
   * @return 日期
   */
  public static String getDate(Date date) {
    return DateToString(date, DateStyle.YYYY_MM_DD);
  }

  /**
   * 获取日期的时间。默认HH:mm:ss格式。失败返回null。
   *
   * @param date 日期字符串
   * @return 时间
   */
  public static String getTime(String date) {
    return StringToString(date, DateStyle.HH_MM_SS);
  }

  /**
   * 获取日期的时间。默认HH:mm:ss格式。失败返回null。
   *
   * @param date 日期
   * @return 时间
   */
  public static String getTime(Date date) {
    return DateToString(date, DateStyle.HH_MM_SS);
  }

  /**
   * 获取当月第一天
   */

  public static String getMonthFirst(Date date) {
    SimpleDateFormat format = new SimpleDateFormat(DateStyle.YYYY_MM_DD_HH_MM_SS.getValue());
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.add(Calendar.MONTH, 0);
    c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
    c.set(Calendar.HOUR_OF_DAY, 00);
    c.set(Calendar.MINUTE, 00);
    c.set(Calendar.SECOND, 00);
    String first = format.format(c.getTime());
    return first;
  }

  /**
   * 获取当月最后一天
   */
  public static String getMonthLast(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
//        calendar.set(Calendar.DAY_OF_MONTH, 1);
//        calendar.add(Calendar.MONTH, 1);
//        calendar.add(Calendar.DAY_OF_MONTH, -1);
    //calendar.add(Calendar.HOUR,23);
//        calendar.add(Calendar.MINUTE,59);
//        calendar.add(Calendar.SECOND,59);
    Date lastDayOfMonth = calendar.getTime();
    return DateToString(lastDayOfMonth, DateStyle.YYYY_MM_DD_HH_MM_SS);
  }

  /**
   * 获取两个日期相差的天数
   *
   * @param date      日期字符串
   * @param otherDate 另一个日期字符串
   * @return 相差天数。如果失败则返回-1
   */
  public static int getIntervalDays(String date, String otherDate) {
    return getIntervalDays(StringToDate(date), StringToDate(otherDate));
  }

  /**
   * @param date      日期
   * @param otherDate 另一个日期
   * @return 相差天数。如果失败则返回-1
   */
  public static int getIntervalDays(Date date, Date otherDate) {
    int num = -1;
    Date dateTmp = DateUtil.StringToDate(DateUtil.getDate(date), DateStyle.YYYY_MM_DD);
    Date otherDateTmp = DateUtil.StringToDate(DateUtil.getDate(otherDate), DateStyle.YYYY_MM_DD);
    if (dateTmp != null && otherDateTmp != null) {
      long time = Math.abs(dateTmp.getTime() - otherDateTmp.getTime());
      num = (int) (time / (24 * 60 * 60 * 1000));
    }
    return num;
  }

  /**
   * @param startTime 日期
   * @param endTime   另一个日期
   * @return 相差分钟
   */
  public static int getIntervalMinute(Date startTime, Date endTime) {
    if (startTime == null || endTime == null) {
      return -1;
    }
    long time = Math.abs(startTime.getTime() - endTime.getTime());
    return (int) (time / (60 * 1000));
  }

  /**
   * 减去一个月
   *
   * @param date 当前日期
   * @return 当前日期减去一个月之后的日期
   */
  public static Date minusOneMonth(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.MONTH, -1);
    return cal.getTime();
  }

  public static Date getDateEnd(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, (date.getYear() + 1900));
    calendar.set(Calendar.MONTH, date.getMonth());
    calendar.set(Calendar.DATE, date.getDate());
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    return calendar.getTime();
  }

  public static Date getDateStart(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, (date.getYear() + 1900));
    calendar.set(Calendar.MONTH, date.getMonth());
    calendar.set(Calendar.DATE, date.getDate());
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    return calendar.getTime();
  }


  public static Calendar getLocalTime(Date date) {
    Calendar localTime = Calendar.getInstance();
    localTime.setTime(date);
    return localTime;
  }

  /**
   * 功能：得到当前日期 格式为：xxxx-yy-zz (eg: 2007-12-05)<br>
   *
   * @return String
   * @author pure
   */
  public static String today(Date date) {
    Calendar localTime = getLocalTime(date);
    String strY = null;
    String strZ = null;
    x = localTime.get(Calendar.YEAR);
    y = localTime.get(Calendar.MONTH) + 1;
    z = localTime.get(Calendar.DATE);
    strY = y >= 10 ? String.valueOf(y) : ("0" + y);
    strZ = z >= 10 ? String.valueOf(z) : ("0" + z);
    return x + "-" + strY + "-" + strZ;
  }

  /**
   * 功能：得到当前月份月初 格式为：xxxx-yy-zz (eg: 2007-12-01)<br>
   *
   * @return String
   * @author pure
   */
  public static String thisMonth(Date date) {
    Calendar localTime = getLocalTime(date);
    String strY = null;
    x = localTime.get(Calendar.YEAR);
    y = localTime.get(Calendar.MONTH) + 1;
    strY = y >= 10 ? String.valueOf(y) : ("0" + y);
    return x + "-" + strY + "-01";
  }

  /**
   * 功能：得到当前月份月底 格式为：xxxx-yy-zz (eg: 2007-12-31)<br>
   *
   * @return String
   * @author pure
   */
  public static String thisMonthEnd(Date date) {
    Calendar localTime = getLocalTime(date);
    String strY = null;
    String strZ = null;
    boolean leap = false;
    x = localTime.get(Calendar.YEAR);
    y = localTime.get(Calendar.MONTH) + 1;
    if (y == 1 || y == 3 || y == 5 || y == 7 || y == 8 || y == 10 || y == 12) {
      strZ = "31";
    }
    if (y == 4 || y == 6 || y == 9 || y == 11) {
      strZ = "30";
    }
    if (y == 2) {
      leap = leapYear(x);
      if (leap) {
        strZ = "29";
      } else {
        strZ = "28";
      }
    }
    strY = y >= 10 ? String.valueOf(y) : ("0" + y);
    return x + "-" + strY + "-" + strZ;
  }

  /**
   * 功能：得到当前季度季初 格式为：xxxx-yy-zz (eg: 2007-10-01)<br>
   *
   * @return String
   * @author pure
   */
  public static String thisSeason(Date date) {
    Calendar localTime = getLocalTime(date);
    String dateString = "";
    x = localTime.get(Calendar.YEAR);
    y = localTime.get(Calendar.MONTH) + 1;
    if (y >= 1 && y <= 3) {
      dateString = x + "-" + "01" + "-" + "01";
    }
    if (y >= 4 && y <= 6) {
      dateString = x + "-" + "04" + "-" + "01";
    }
    if (y >= 7 && y <= 9) {
      dateString = x + "-" + "07" + "-" + "01";
    }
    if (y >= 10 && y <= 12) {
      dateString = x + "-" + "10" + "-" + "01";
    }
    return dateString;
  }

  /**
   * 功能：得到当前季度季末 格式为：xxxx-yy-zz (eg: 2007-12-31)<br>
   *
   * @return String
   * @author pure
   */
  public static String thisSeasonEnd(Date date) {
    Calendar localTime = getLocalTime(date);
    String dateString = "";
    x = localTime.get(Calendar.YEAR);
    y = localTime.get(Calendar.MONTH) + 1;
    if (y >= 1 && y <= 3) {
      dateString = x + "-" + "03" + "-" + "31";
    }
    if (y >= 4 && y <= 6) {
      dateString = x + "-" + "06" + "-" + "30";
    }
    if (y >= 7 && y <= 9) {
      dateString = x + "-" + "09" + "-" + "30";
    }
    if (y >= 10 && y <= 12) {
      dateString = x + "-" + "12" + "-" + "31";
    }
    return dateString;
  }

  /**
   * 功能：得到当前年份年初 格式为：xxxx-yy-zz (eg: 2007-01-01)<br>
   *
   * @return String
   * @author pure
   */
  public static String thisYear(Date date) {
    Calendar localTime = getLocalTime(date);
    x = localTime.get(Calendar.YEAR);
    return x + "-01" + "-01";
  }

  /**
   * 功能：得到当前年份年底 格式为：xxxx-yy-zz (eg: 2007-12-31)<br>
   *
   * @return String
   * @author pure
   */
  public static String thisYearEnd(Date date) {
    Calendar localTime = getLocalTime(date);
    x = localTime.get(Calendar.YEAR);
    return x + "-12" + "-31";
  }

  /**
   * 功能：判断输入年份是否为闰年<br>
   *
   * @param year
   * @return 是：true  否：false
   * @author pure
   */
  public static boolean leapYear(int year) {
    boolean leap;
    if (year % 4 == 0) {
      if (year % 100 == 0) {
        if (year % 400 == 0) leap = true;
        else leap = false;
      } else leap = true;
    } else leap = false;
    return leap;
  }

  /**
   * 取得时间差返回天数
   */
  public static long DateTimeSpace1(Date starttime, Date endtime) {
    if (starttime == null || endtime == null) {
      return 0l;
    }
    long startTime = starttime.getTime();
    long endTime = endtime.getTime();

    long spacetime = (endTime - startTime) / 1000 / 60 / 60;
    // spacetime = Math.abs(endtimes - starttimes) / 1000 / 60 / 60 ;
    if (spacetime < 24 && spacetime > 0)
      return 1;
    else
      return spacetime / 24.0 - spacetime / 24 > 0 ? spacetime / 24 + 1
        : spacetime / 24;
  }

//    public static void main(String[] args) {
//        System.out.println( DateUtil.DateTimeSpace1(DateUtil.getDateStart(new Date()), DateUtil.getDateEnd(new Date())));
  //System.out.println(getIntervalMinute(new Date(),addHour(new Date(),3)));
//        Date date = new Date();
//        //昨日发票款
//        String lastDate = DateUtil.DateToString(DateUtil.addDay(date, -1), DateStyle.YYYY_MM_DD);
//        System.out.println("lastDate:" + lastDate + " 00:00:00" + "," + lastDate + " 23:59:59");
//        //上月发票款
//        Date lastMonth = DateUtil.addMonth(date, -1);
//        System.out.println("lastMonth:" + DateUtil.getMonthFirst(lastMonth) + "," + DateUtil.getMonthLast(lastMonth));
//        //本年度发票款
//        System.out.println("thisYear:" + DateUtil.thisYear(new Date()) + "," + DateUtil.thisYearEnd(new Date()));
//        //上一年度发票款
//        Date lastYear = DateUtil.addYear(new Date(), -1);
//        System.out.println("lastYear:" + DateUtil.thisYear(lastYear) + "," + DateUtil.thisYearEnd(lastYear));
//    }
}
