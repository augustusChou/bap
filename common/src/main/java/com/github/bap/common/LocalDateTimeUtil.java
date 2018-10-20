package com.github.bap.common;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author 周广
 **/
public class LocalDateTimeUtil {

    private static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * LocalDateTime 转 Date
     *
     * @param time 输入时间
     * @return 转换为Date格式输出
     */
    public static Date localDateTimeConvertToDate(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 将LocalDate转Date
     *
     * @param localDate 纯日期
     * @return 只有日期，时间为 00:00:00
     */
    public static Date localDateConvertDate(LocalDate localDate) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDate.atStartOfDay(zoneId);
        return Date.from(zdt.toInstant());
    }


    /**
     * Date 转LocalDate
     *
     * @param date Date类型时间
     * @return LocalDate （只含日期）
     */
    public static LocalDate dateConvertLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalDate();
    }

    /**
     * Date 转LocalDateTime
     *
     * @param date Date类型时间
     * @return LocalDateTime 日期事件
     */
    public static LocalDateTime dateConvertLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * LocalDateTime转字符串 yyyy-MM-dd HH:mm:ss
     *
     * @param time LocalDateTime事件
     * @return yyyy-MM-dd HH:mm:ss 字符串
     */
    public static String localDateTimeConvertString(LocalDateTime time) {
        return FORMATTER.format(time);
    }


    /**
     * date 转 yyyy-MM-dd HH:mm:ss字符串
     *
     * @param date 旧的时间类型
     * @return yyyy-MM-dd HH:mm:ss字符串
     */
    public static String convertString(Date date) {
        return FORMATTER.format(dateConvertLocalDateTime(date));
    }

    /**
     * 将  yyyy-MM-dd HH:mm:ss字符串 转化为 LocalDateTime
     *
     * @param time yyyy-MM-dd HH:mm:ss
     * @return LocalDateTime
     */
    public static LocalDateTime parse(String time) {
        return LocalDateTime.from(FORMATTER.parse(time));
    }

    public static Timestamp parseToTimestamp(String time) {
        return new Timestamp(localDateTimeConvertToDate(LocalDateTime.from(FORMATTER.parse(time))).getTime());
    }

    public static Date parseToDate(String time) {
        return localDateTimeConvertToDate(LocalDateTime.from(FORMATTER.parse(time)));
    }
}
