package com.ipanel.tv.web.service.impl.pojo;

import com.ipanel.tv.web.util.JsonUtil;
import com.ipanel.tv.web.util.jpa.partial.QueryColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * @author luzh
 * Create: 2019-12-18 22:32
 * Modified By:
 * Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class StatusBO extends DataBO {
    @QueryColumn
    private Long timestamp;
    @QueryColumn
    private String minute;
    @QueryColumn
    private String status;

    @Override
    public long getTimestamp() {
        return this.timestamp;
    }

    public Map<String, Integer> getStatus() {
        return JsonUtil.getStringIntegerMap(this.status);
    }


}
