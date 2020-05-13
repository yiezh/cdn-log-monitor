package com.ipanel.tv.web.service;

import com.ipanel.tv.web.controller.response.*;
import com.ipanel.tv.web.log.StatisticsBO;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author luzh
 * Create: 2019-12-17 11:05
 * Modified By:
 * Description:
 */
public interface StatisticsLogService {

    /**
     * 保存数据
     *
     * @param bo bo
     */
    void save(StatisticsBO bo);

    /**
     * 聚合数据
     *
     * @param bo bo
     */
    @Transactional
    void merge(StatisticsBO bo);

    /**
     * 根据域名（可选）开始时间、结束时间、时间粒度查询访问数据
     *
     * @param host             域名
     * @param startEpochSecond 开始时间（Unix时间戳 秒）
     * @param endEpochSecond   结束时间（Unix时间戳 秒）
     * @param timeGranularity  时间粒度（分钟）
     * @return list
     */
    DataListVO<AccessVO> getAccessData(String host, long startEpochSecond, long endEpochSecond, int timeGranularity);

    /**
     * 根据域名（可选）开始时间、结束时间、时间粒度查询缓存命中数据
     *
     * @param host             域名
     * @param startEpochSecond 开始时间（Unix时间戳 秒）
     * @param endEpochSecond   结束时间（Unix时间戳 秒）
     * @param timeGranularity  时间粒度（分钟）
     * @return list
     */
    DataListVO<HitVO> getHitData(String host, long startEpochSecond, long endEpochSecond, int timeGranularity);

    /**
     * 根据域名（可选）开始时间、结束时间、时间粒度查询状态码数据
     *
     * @param host             域名
     * @param startEpochSecond 开始时间（Unix时间戳 秒）
     * @param endEpochSecond   结束时间（Unix时间戳 秒）
     * @param timeGranularity  时间粒度（分钟）
     * @return list
     */
    DataListVO<StatusVO> getStatusData(String host, long startEpochSecond, long endEpochSecond, int timeGranularity);

    DataListVO<UpstreamVO> getUpstreamData(String host, long startEpochSecond, long endEpochSecond, int timeGranularity);
}
