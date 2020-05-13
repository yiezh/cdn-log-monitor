package com.ipanel.tv.web.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ipanel.tv.web.service.impl.pojo.DataBO;
import com.ipanel.tv.web.service.impl.pojo.HitBO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author luzh
 * Create: 2019-12-18 20:47
 * Modified By:
 * Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class HitVO extends DataVO {
    private long hitBytes;
    private long hitCount;
    private long allBytes;
    private long allCount;

    public HitVO(long startTime, long endTime) {
        super(startTime, endTime);
    }

    private void add(HitBO bo) {
        this.hitBytes += bo.getHitBytes();
        this.hitCount += bo.getHitCount();
        this.allBytes += bo.getAllBytes();
        this.allCount += bo.getAllCount();
    }

    @Override
    public <T extends DataBO> void add(T t) {
        if (t instanceof HitBO) {
            add((HitBO) t);
        }
        update(t, this);
    }
}
