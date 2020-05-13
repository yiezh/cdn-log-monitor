package com.ipanel.tv.web.service.impl.pojo;

import com.ipanel.tv.web.util.jpa.partial.QueryColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;

/**
 * @author luzh
 * Create: 2019-12-20 14:19
 * Modified By:
 * Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class UpstreamBO extends DataBO {
    @QueryColumn
    private Long timestamp;
    @QueryColumn
    private String minute;
    @QueryColumn
    private Long upstreamBytes;
    @QueryColumn
    private Integer upstreamCount;

    @Override
    public long getTimestamp() {
        return this.timestamp;
    }
}
