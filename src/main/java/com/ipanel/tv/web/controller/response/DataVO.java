package com.ipanel.tv.web.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ipanel.tv.web.service.impl.pojo.DataBO;
import lombok.Data;

/**
 * @author luzh
 * Create: 2019-12-18 20:11
 * Modified By:
 * Description:
 */
@Data
public abstract class DataVO {
    private long start;
    private long end;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String startMinute;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String endMinute;
    @JsonIgnore
    private long previousDataTimestamp;
    private int sourceCount;

    DataVO(long startTime, long endTime) {
        this.start = startTime;
        this.end = endTime;
    }

    public abstract <T extends DataBO> void add(T t);

    static <T extends DataBO> void update(T t, DataVO vo) {
        if (0 == vo.previousDataTimestamp) {
            vo.previousDataTimestamp = t.getTimestamp();
        }
        if (null == vo.startMinute) {
            vo.startMinute = t.getMinute();
        }
        if (null == vo.endMinute || vo.previousDataTimestamp < t.getTimestamp()) {
            vo.endMinute = t.getMinute();
        }
        vo.sourceCount += 1;
    }
}
