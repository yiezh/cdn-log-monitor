package com.ipanel.tv.web.util;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author luzh
 * Create: 2019-12-13 14:06
 * Modified By:
 * Description:
 */
@Slf4j
public class JsonUtil {

    private static final Gson GSON = new Gson();

    protected JsonUtil() {
    }

    public static <T> T fromJson(String value, Class<T> clazz) {
        T t = null;
        try {
            return GSON.fromJson(value, clazz);
        } catch (Exception e) {
            log.error("{}", e.getMessage());
            return null;
        }
    }

    private static <T> T fromJson(String value, Type type) {
        return GSON.fromJson(value, type);
    }

    public static <T> String toJson(T t) {
        return GSON.toJson(t);
    }

    public static Map<String, Integer> getStringIntegerMap(String status) {
        Map<String, Double> map = fromJson(status, (Type) HashMap.class);
        Map<String, Integer> statusMap = new HashMap<>(map.size());
        map.forEach((key, value) -> statusMap.put(key, value.intValue()));
        return statusMap;
    }
}
