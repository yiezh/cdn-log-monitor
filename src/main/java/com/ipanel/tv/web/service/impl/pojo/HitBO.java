package com.ipanel.tv.web.service.impl.pojo;

import com.ipanel.tv.web.util.jpa.partial.QueryColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author luzh
 * Create: 2019-12-18 20:39
 * Modified By:
 * Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class HitBO extends DataBO {
    @QueryColumn
    private Long timestamp;
    @QueryColumn
    private String minute;
    @QueryColumn
    private Long hitBytes;
    @QueryColumn
    private Integer hitCount;
    @QueryColumn
    private Long allBytes;
    @QueryColumn
    private Integer allCount;

    @Override
    public long getTimestamp() {
        return this.timestamp;
    }
}
