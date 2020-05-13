package com.ipanel.tv.web.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ipanel.tv.web.service.impl.pojo.AccessBO;
import com.ipanel.tv.web.service.impl.pojo.DataBO;
import com.ipanel.tv.web.service.impl.pojo.UpstreamBO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author luzh
 * Create: 2019-12-20 14:17
 * Modified By:
 * Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UpstreamVO extends DataVO {
    private long upstreamBytes;
    private long upstreamCount;

    public UpstreamVO(long startTime, long endTime) {
        super(startTime, endTime);
    }

    private void add(UpstreamBO bo) {
        this.upstreamCount += bo.getUpstreamCount();
        this.upstreamBytes += bo.getUpstreamBytes();
    }

    @Override
    public <T extends DataBO> void add(T t) {
        if (t instanceof UpstreamBO) {
            add((UpstreamBO) t);
        }
        update(t, this);
    }
}
