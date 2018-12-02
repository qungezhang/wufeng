package com.planet.common.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @version:1.0.0
 * @Description:时间工具类
 * @author: aiveily
 * @date: 2018/9/11
 */
public class DateTools {

    /**
     * @version 1.0.0
     * @description 将日期类型的对象转换成字符串格式
     * @param date 需要转换的日期对象
     * @param fmt 转换格式，例如"yyyy年MM月dd日 HH时mm分ss秒 E"；默认是"yyyy年MM月dd日"
     * @return 转换后的字符串格式日期
     * @author aiveily
     * @date 2018/9/11 10:53
     */
    public static String parseDateToString(Date date, String fmt) {
        if (null == fmt || "".equals(fmt.trim())) {
            fmt = "yyyy年MM月dd日";
        }
        DateFormat df = new SimpleDateFormat(fmt);
        return df.format(date);
    }

    /**
     * @version 1.0.0
     * @description 将日期字符串转换成Date类型
     * @param strDate 需要转换的日期字符串
     * @param fmt 转换格式，例如"yyyy年MM月dd日 HH时mm分ss秒 E"；默认是"yyyy-MM-dd"
     * @return 转换后的日期格式
     * @author aiveily
     * @date 2018/9/11 10:54
     */
    public static Date parseStringToDate(String strDate, String fmt) {
        if (null == fmt || "".equals(fmt.trim())) {
            fmt = "yyyy-MM-dd";
        }
        SimpleDateFormat df = new SimpleDateFormat(fmt);
        Date date = null;
        try {
            date = df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date parseLongToDate(String timestamp, String fmt) {
        if (null == fmt || "".equals(fmt.trim())) {
            fmt = "yyyy-MM-dd";
        }
        Date date = null;
        try {
            if (null != timestamp) {
                String dateStr = new SimpleDateFormat(fmt).format(new Date(Long.parseLong(timestamp) * 1000));
                SimpleDateFormat df = new SimpleDateFormat(fmt);
                date = df.parse(dateStr);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * @version 1.0.0
     * @description 获取当前年份
     * @return 获取到的当前年份
     * @author aiveily
     * @date 2018/9/11 10:55
     */
    public static String getCurrentYear(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date date = new Date();
        return sdf.format(date);
    }

    /**
     * @version 1.0.0
     * @description 获取上个月时间
     * @param date 传入的时间点
     * @return 上个月的时间
     * @author aiveily
     * @date 2018/9/11 10:57
     */
    public static Date getPreviousMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }

    /**
     * @version 1.0.0
     * @description 获取当前月份的上个月份
     * @return 返回的上个月月份时间
     * @author aiveily
     * @date 2018/9/11 10:59
     */
    public static Date getPreviousMonthByCurrent() {
        return parseStringToDate(DateTools.parseDateToString(DateTools.getPreviousMonth(new Date()), "yyyy-MM"), "yyyy-MM");
    }

    /**
     * @version 1.0.0
     * @description 获取指定月份的第一天
     * @param dateStr 字符型日期
     * @return 指定月份的一天
     * @author aiveily
     * @date 2018/9/11 11:31
     */
    public static String getMonthBeginByString(String dateStr) {
        Date date = parseStringToDate(dateStr, "yyyy-MM");
        return parseDateToString(date, "yyyy-MM") + "-01";
    }

    /**
     * @version 1.0.0
     * @description 获取指定月份的第一天
     * @param date 日期
     * @return 指定月份的第一天
     * @author aiveily
     * @date 2018/9/11 11:35
     */
    public static Date getMonthBeginByString(Date date) {
        return parseStringToDate(parseDateToString(date, "yyyy-MM") + "-01","yyyy-MM-dd");
    }

    /**
     * @version 1.0.0
     * @description 获取指定月份的最后一天
     * @param dateStr 字符型日期
     * @return
     * @author aiveily
     * @date 2018/9/11 11:32
     */
    public static String getMonthEndByString(String dateStr) {
        Date date = parseStringToDate(getMonthBeginByString(dateStr),"yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return parseDateToString(calendar.getTime(),"yyyy-MM-dd");
    }

    /**
     * @version 1.0.0
     * @description 获取指定月份的最后一天
     * @param date 字符型日期
     * @return 月份的最后一天
     * @author aiveily
     * @date 2018/9/11 11:34
     */
    public static Date getMonthEndByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
    }

    /**
     * @version 1.0.0
     * @description 获取指定日期的第几天前的日期
     * @param dateStr 指定的日期格式
     * @param num 第几天
     * @return 日期
     * @author aiveily
     * @date 2018/9/11 11:36
     */
    public static String getDateAfter(String dateStr, int num){
        Date date = parseStringToDate(dateStr, "yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -(num));
        return parseDateToString(cal.getTime(), "yyyy-MM-dd");
    }

    /**
     * @version 1.0.0
     * @description 获取指定月份的总天数
     * @param month 指定月份
     * @return 天数
     * @author aiveily
     * @date 2018/9/12 16:47
     */
    public static int getDaysByMonth(String month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateTools.parseStringToDate(month, "yyyy-MM"));
        calendar.set(Calendar.DATE, 1);//把日期设置为当月第一天
        calendar.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        return calendar.get(Calendar.DATE);
    }

    /**
     * @version 1.0.0
     * @description 获取当天的0时
     * @param
     * @return
     * @author aiveily
     * @date 2018/9/17 11:09
     */
    public static Date getTimesmorn() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return  cal.getTime();
    }

    /**
     * @version 1.0.0
     * @description 获取当天的24时
     * @author aiveily
     * @date 2018/9/11 11:38
     */
    public static Date getTimesnight() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return  cal.getTime();
    }

    /**
     * @version 1.0.0
     * @description 获取两个日期之差
     * @param oneDate 第一个日期
     * @param twoDate 第二个日期
     * @return 两个日期相差值
     * @author aiveily
     * @date 2018/9/11 11:00
     */
    public static long getDateQuot(String oneDate, String twoDate) {
//        long quot = 0;
//        Date date1 = DateTools.parseStringToDate(oneDate, "yyyy-MM-dd");
//        Date date2 = DateTools.parseStringToDate(twoDate, "yyyy-MM-dd");
//        quot = date1.getTime() - date2.getTime();
//        quot = quot / 1000 / 60 / 60 / 24;
//        return quot;

        Calendar cal = Calendar.getInstance();
        cal.setTime(DateTools.parseStringToDate(oneDate,"yyyy-MM-dd"));
        long time1 = cal.getTimeInMillis();
        Calendar ca2 = Calendar.getInstance();
        ca2.setTime(DateTools.parseStringToDate(twoDate,"yyyy-MM-dd"));
        long time2 = ca2.getTimeInMillis();
        long between_days = (time2 - time1)/(1000*3600*24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * @version 1.0.0
     * @description 获取两个月份之差
     * @param oneDate 第一个月份
     * @param twoDate 第二个月份
     * @return 两个月份相差值
     * @author aiveily
     * @date 2018/9/11 11:02
     */
    public static long getMonthQuot(String oneDate, String twoDate) {
        long quot = 0;
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(DateTools.parseStringToDate(oneDate, "yyyy-MM"));
        long yearDate1 = cal1.get(Calendar.YEAR);
        long monthDate1 = cal1.get(Calendar.MONTH) + 1;
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(DateTools.parseStringToDate(twoDate, "yyyy-MM"));
        long yearDate2 = cal2.get(Calendar.YEAR);
        long monthDate2 = cal2.get(Calendar.MONTH) + 1;
        quot = (yearDate1 - yearDate2) * 12 + (monthDate1 - monthDate2);
        return quot;
    }

    /**
     * @version 1.0.0
     * @description 查询两个时间的中间月份集合
     * @param startStr 开始日期
     * @param endStr 结束日期
     * @return 中间月份的集合
     * @author aiveily
     * @date 2018/9/12 9:33
     */
    public static List<String> getAllMonths(String startStr, String endStr)  throws ParseException {
        ArrayList<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//格式化为年月

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(sdf.parse(startStr));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(sdf.parse(endStr));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf.format(curr.getTime()));
            curr.add(Calendar.MONTH, 1);
        }
        return result;
    }

    /**
     * @version 1.0.0
     * @description 查询两个时间的中间每周集合
     * @param startStr 开始日期
     * @param endStr 结束日期
     * @return 中间月份的集合
     * @author aiveily
     * @date 2018/9/12 9:33
     */
    public static List<String> getAllWeeks(String startStr, String endStr)  throws ParseException {
        ArrayList<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//格式化为年月

        System.out.println("输出endStr----"+endStr);

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(sdf.parse(startStr));

        max.setTime(sdf.parse(endStr));

        Calendar curr = min;
        System.out.println("max----"+sdf.format(max.getTime()));
        while (curr.before(max)) {
            System.out.println("输出curr----"+sdf.format(curr.getTime()));
            result.add(sdf.format(curr.getTime()));
            curr.add(Calendar.WEEK_OF_MONTH, 1);
        }
        return result;
    }

    /**
     * @version 1.0.0
     * @description 获取有多少个工作日
     * @param
     * @return
     * @author aiveily
     * @date 2018/9/12 13:54
     */
    public static Map<Object, Integer> getWorkDayByMonth (String dateStr) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateTools.parseStringToDate(dateStr,"yyyy-MM"));
        Map<Object, Integer> map = new HashMap<>();
        int workDays = 0;
        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        try {
            calendar.set(Calendar.DATE, 1);//从每月1号开始
            for (int i = 0; i < days; i++) {
                int day = calendar.get(Calendar.DAY_OF_WEEK);
                if (!(day == Calendar.SUNDAY || day == Calendar.SATURDAY)) {
                    workDays++;
                }
                calendar.add(Calendar.DATE, 1);
            }
            map.put("workDaysAmount", workDays);//工作日
            map.put("year", calendar.get(Calendar.YEAR));//实时年份
            map.put("month", calendar.get(Calendar.MONTH));//实时月份
            map.put("daysAmount", days);//自然日
            map.put("weeksAmount", calendar.getActualMaximum(Calendar.WEEK_OF_MONTH));//周
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * @version 1.0.0
     * @description 获取两个数之间的工作日
     * @param strStartDate 开始日期
     * @param strEndDate 结束日期
     * @author aiveily
     * @date 2018/9/12 14:46
     */
    public static int getDutyDays (String strStartDate, String strEndDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cl1 = Calendar.getInstance();
        Calendar cl2 = Calendar.getInstance();

        try {
            cl1.setTime(df.parse(strStartDate));
            cl2.setTime(df.parse(strEndDate));
        } catch (ParseException e) {
            System.out.println("日期格式非法");
            e.printStackTrace();
        }
        int count = 0;
        while (cl1.compareTo(cl2) <= 0) {
            if (cl1.get(Calendar.DAY_OF_WEEK) != 7 && cl1.get(Calendar.DAY_OF_WEEK) != 1)
                count++;
            cl1.add(Calendar.DAY_OF_MONTH, 1);
        }
        return count;
    }

    /**
     * 获取当前时间多少分钟后的下一个时间
     * @param day 当前时间
     * @param x 相隔的多少分钟
     * @return
     */
    public static String getDateMinut(String day, int x) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 24小时制
        //出参的格式
        SimpleDateFormat newFormat = new SimpleDateFormat("HH:mm:ss");// 24小时制
        Date date = null;
        try {
            date = format.parse(day);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (date == null)
            return "";
        System.out.println("front:" + format.format(date));
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, x);// 24小时制
        //得到结算后的结果 yyyy-MM-dd HH:mm
        date = cal.getTime();
        System.out.println("after:" + format.format(date));
        cal = null;
        //转换结果的格式 HH:mm
        return newFormat.format(date);
    }

    /**
     * 生成corn表达式
     * @param date 当前时间
     * @return
     */
    public static String getCronByPatternDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("ss mm HH * * ? *");
        String formatTimeStr = null;
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }

    /**
     * 测试
     * @param args
     */
    public static void main(String[] args){
        String cron = getDateMinut(DateTools.parseDateToString(new Date(),"yyyy-MM-dd HH:mm:ss"),1);
        System.out.println(cron);
    }
}
