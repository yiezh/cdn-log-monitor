package com.ipanel.tv.web.error;

/**
 * @author luzh
 * Create: 2019-05-30 15:22
 * Modified By:
 * Description:
 */
public interface ErrorFunction<O, T, R> {

    R apply(O o, T t);
}
