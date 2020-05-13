package com.ipanel.tv.web.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ipanel.tv.web.service.impl.pojo.AccessBO;
import com.ipanel.tv.web.service.impl.pojo.DataBO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author luzh
 * Create: 2019-12-18 10:11
 * Modified By:
 * Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AccessVO extends DataVO {
    private long allBytes;
    private long allCount;

    public AccessVO(long startTime, long endTime) {
        super(startTime, endTime);
    }

    private void add(AccessBO bo) {
        this.allBytes += bo.getAllBytes();
        this.allCount += bo.getAllCount();
    }

    @Override
    public <T extends DataBO> void add(T t) {
        if (t instanceof AccessBO) {
            add((AccessBO) t);
        }
        update(t, this);
    }
}
