package com.ipanel.tv.web.controller.response;

import lombok.Data;

/**
 * @author luzh
 * Create: 2019-12-18 20:45
 * Modified By:
 * Description:
 */
@Data
public class DataListVO<T extends DataVO> {
    private T[] list;

    public DataListVO(T[] t) {
        this.list = t;
    }
}
