package com.ipanel.tv.web.controller.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author luzh
 * Create: 2019-12-18 10:16
 * Modified By:
 * Description:
 */
@Data
public class CommonReq {
    private String domain;
    @NotNull(message = "开始时间不能为空")
    private String start;
    @NotNull(message = "结束时间不能为空")
    private String end;
    @NotNull(message = "时间粒度（分钟）不能为空")
    private Integer timeGranularity;

    public Integer getTimeGranularity() {
        return timeGranularity <= 0 ? 1 : timeGranularity;
    }
}
