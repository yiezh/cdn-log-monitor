package com.ipanel.tv.web.service;

import com.ipanel.tv.web.log.StatisticsBO;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author luzh
 * Create: 2019-12-17 17:20
 * Modified By:
 * Description:
 */
public interface MergeService {

    void saveStatisticsBO(StatisticsBO bo);

    @Transactional
    void merge();
}
