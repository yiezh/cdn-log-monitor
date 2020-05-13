package com.ipanel.tv.web.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author luzh
 * Create: 2019-12-17 18:33
 * Modified By:
 * Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsLogGroupKey {
    private long time;
    private String domain;
}
