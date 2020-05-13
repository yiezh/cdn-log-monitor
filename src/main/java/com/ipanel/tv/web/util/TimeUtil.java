package com.ipanel.tv.web.util;

import com.ipanel.tv.web.exception.GlobalException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author luzh
 * Create: 2019-08-23 09:56
 * Modified By:
 * Description:
 */
public class TimeUtil {

    /**
     * 通用时间格式
     */
    public static final DateTimeFormatter COMMON_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static ZoneOffset systemDefaultZoneId;

    /**
     * LocalDateTime转时间戳
     *
     * @param dateTime utc毫秒值
     * @return LocalDateTime
     */
    public static Long convertDateTime(LocalDateTime dateTime) {
        return dateTime.toInstant(getSystemDefaultZoneId()).toEpochMilli();
    }

    /**
     * 时间戳转LocalDateTime
     *
     * @param dateTime utc毫秒值
     * @return LocalDateTime
     */
    public static LocalDateTime convertDateTime(Long dateTime) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(dateTime), getSystemDefaultZoneId());
    }

    public static LocalDateTime parse(String time, DateTimeFormatter dateTimeFormatter) throws GlobalException {
        try {
            return LocalDateTime.parse(time, dateTimeFormatter);
        } catch (Exception e) {
            throw new GlobalException("时间格式不正确！");
        }
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @param time 时间
     * @return time
     * @throws GlobalException e
     */
    public static LocalDateTime parse(String time) throws GlobalException {
        return parse(time, COMMON_FORMATTER);
    }

    /**
     * 获取系统默认时区
     *
     * @return 时区ZoneOffset
     */
    public static ZoneOffset getSystemDefaultZoneId() {
        if (systemDefaultZoneId == null) {
            // systemDefaultZoneId = Clock.systemDefaultZone().getZone().getRules().getOffset(Instant.now());
            systemDefaultZoneId = ZoneOffset.from(ZonedDateTime.now());
        }
        return systemDefaultZoneId;
    }

    public static long minuteEpochMilli(int minute) {
        if (minute <= 0) {
            return 0L;
        }
        return minute * 60 * 1000;
    }
}
