package com.ipanel.tv.web.log;

import com.google.gson.Gson;
import com.ipanel.tv.web.util.TimeUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author luzh
 * Create: 2019-12-10 16:54
 * Modified By:
 * Description:
 */
public class LogConverter {

    private static final Gson GSON = new Gson();
    private static final DateTimeFormatter DATE_TIME_FORMATTER_ORIGIN = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH);
    private static final DateTimeFormatter DATE_TIME_FORMATTER_MINUTE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    protected LogConverter() {
    }

    public static LogBO convertTime(LogBO entity) {
        LocalDateTime dateTime = LocalDateTime.parse(entity.getLogTime(), DATE_TIME_FORMATTER_ORIGIN).withSecond(0);
        entity.setTimestamp(TimeUtil.convertDateTime(dateTime));
        entity.setMinute(DATE_TIME_FORMATTER_MINUTE.format(dateTime));
        return entity;
    }
}
