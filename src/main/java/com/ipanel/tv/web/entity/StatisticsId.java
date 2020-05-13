package com.ipanel.tv.web.entity;

import com.ipanel.tv.web.log.StatisticsBO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author luzh
 * Create: 2019-12-17 12:10
 * Modified By:
 * Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsId implements Serializable {
    private static final long serialVersionUID = 5613991172483063637L;

    private Long timestamp;
    private String domain;

    public StatisticsId(StatisticsBO bo) {
        this.timestamp = bo.getTimestamp();
        this.domain = bo.getDomain();
    }
}
