package com.ipanel.tv.web.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ipanel.tv.web.service.impl.pojo.DataBO;
import com.ipanel.tv.web.service.impl.pojo.StatusBO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luzh
 * Create: 2019-12-18 22:22
 * Modified By:
 * Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StatusVO extends DataVO {
    private Map<String, Integer> status = new HashMap<>();

    public StatusVO(long startTime, long endTime) {
        super(startTime, endTime);
    }

    private void add(StatusBO bo) {
        bo.getStatus().forEach((key, value) -> this.status.put(key, this.status.getOrDefault(key, 0) + value));
    }

    @Override
    public <T extends DataBO> void add(T t) {
        if (t instanceof StatusBO) {
            add((StatusBO) t);
        }
        update(t, this);
    }
}
