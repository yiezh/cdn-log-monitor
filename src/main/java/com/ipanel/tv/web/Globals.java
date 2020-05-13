package com.ipanel.tv.web;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author luzh
 * Create: 2019-05-27 16:44
 * Modified By:
 * Description:
 */
public class Globals {

    /**
     * 获取异常 cause
     *
     * @param e 异常
     * @return cause
     */
    public static Throwable getOriginException(Throwable e) {
        while (e.getCause() != null) {
            e = e.getCause();
        }
        return e;
    }

    public static Optional<Integer> convertString2Integer(String number) {
        try {
            return Optional.of(new Integer(number));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static String getRequestInfo(HttpServletRequest request) {
        return request.getMethod() + " request: " + request.getRequestURL().toString();
    }
}
