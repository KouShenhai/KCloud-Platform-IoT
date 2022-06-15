package io.laokou.common.utils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.text.ParseException;
import java.util.Date;
/**
 * 日期处理
 * @author  Kou Shenhai
 */
public class DateUtil {

    /**
     * 时间格式(yyyy-MM-dd)
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public final static String DATE_TIME = "yyyyMMddHHmmss";

    public final static String YM_DATE_TIME = "yyyyMM";

    /**
     * 日期格式胡 日期格式为 yyyy-MM-dd
     * @param date 日期
     * @return 返回yyyy-MM-dd格式日期
     */
    public static String format(Date date){
        return format(date,DATE_PATTERN);
    }

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     * @param date 日期
     * @param pattern 格式 如：DateUtil.DATE_TIME_PATTERN
     * @return 返回yyyy-MM-dd格式日期
     */
    public static String format(Date date,String pattern){
        if (null != date){
            return DateFormatUtils.format(date,pattern);
        }
        return null;
    }

    /**
     * 日期解析
     * @param date 日期
     * @param pattern 格式， 如：DateUtil.DATE_TIME_PATTERN
     * @return 返回Date
     */
    public static Date parse(String date,String pattern) {
        try {
            return DateUtils.parseDateStrictly(date,pattern);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字符串转换日期
     * @param strDate 日期字符串
     * @param pattern 日期的格式，如：DateUtil.DATE_PATTERN
     */
    public static Date stringToDDate(String strDate,String pattern){
        if (StringUtils.isBlank(strDate)){
            return null;
        }
        DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
        return fmt.parseDateTime(strDate).toDate();
    }

    /**
     * 根据周期，获取开始周期、结束日期
     * @param week 周期 0本周 -1上周 -2上上周 1下周 2下下周
     * @return 返回date[0]开始日期、date[1]结束日期
     */
    public static Date[] getWeekStartAndEnd(int week){
        DateTime dateTime = new DateTime();
        LocalDate date = new LocalDate(dateTime.plusWeeks(week));

        date = date.dayOfWeek().withMinimumValue();
        Date beginDate = date.toDate();
        Date endDate = date.plusDays(6).toDate();
        return new Date[]{beginDate,endDate};
    }

    /**
     * 对日期的【秒】进行加/减
     * @param date 日期
     * @param seconds 秒数，负数为减
     * @return 加/减几秒后的日期
     */
    public static Date addDateSeconds(Date date , int seconds){
        DateTime dateTime = new DateTime(date);
        return dateTime.plusSeconds(seconds).toDate();
    }

    /**
     * 对日期的【分秒】进行加/减
     * @param date 日期
     * @param minutes 分钟数，负数为减
     * @return 加/减几分钟后的日期
     */
    public static Date addDateMinutes(Date date , int minutes){
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMinutes(minutes).toDate();
    }

    /**
     * 对日期的【小时】进行加/减
     * @param date 日期
     * @param hours 小时数，负数为减
     * @return 加/减几个小时后的日期
     */
    public static Date addDateHours(Date date,int hours) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusHours(hours).toDate();
    }
        /**
         * 对日期的【天】机械能加/减
         * @param date 日期
         * @param days 天数，负数为减
         * @return 加/减几天后的日期
         */
        public static  Date addDateDays(Date date,int days){
            DateTime dateTime = new DateTime(date);
            return dateTime.plusDays(days).toDate();
        }

    /**
     * 对日期的【周】进行加/减
     * @param date 日期
     * @param weeks 周数 ，负数为减
     * @return 加/减几周后的日期
     */
    public static  Date addDateWeeks(Date date,int weeks){
        DateTime dateTime = new DateTime(date);
        return dateTime.plusWeeks(weeks).toDate();
    }

    /**
     * 对日期的【月】进行加/减
     * @param date 日期
     * @param months 月数，负数为减
     * @return 加/减几月后的日期
     */
    public static Date addDateMonths(Date date ,int months){
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMonths(months).toDate();
    }

    /**
     * 对日期的【年】进行加/减
     * @param date 日期
     * @param years 年数，负数为减
     * @return 加/减几年后的日期
     */
    public static Date addDateYears(Date date,int years){
        DateTime dateTime = new DateTime(date);
        return dateTime.plusYears(years).toDate();
    }

}
