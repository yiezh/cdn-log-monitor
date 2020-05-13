package com.ipanel.tv.web.controller.response;

/**
 * @author luzh
 * Create: 2019-12-18 21:35
 * Modified By:
 * Description:
 */
public interface DataConstructor<T extends DataVO> {

    T construct(long startTime, long endTime);
}
