package com.ipanel.tv.web.service.impl.pojo;

import com.ipanel.tv.web.util.jpa.partial.QueryColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author luzh
 * Create: 2019-12-18 14:46
 * Modified By:
 * Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class AccessBO extends DataBO {
    @QueryColumn
    private Long timestamp;
    @QueryColumn
    private String minute;
    @QueryColumn
    private Long allBytes;
    @QueryColumn
    private Integer allCount;

    @Override
    public long getTimestamp() {
        return this.timestamp;
    }
}
