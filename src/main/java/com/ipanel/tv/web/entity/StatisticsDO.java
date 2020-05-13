package com.ipanel.tv.web.entity;

import com.ipanel.tv.web.log.StatisticsBO;
import com.ipanel.tv.web.util.JsonUtil;
import com.ipanel.tv.web.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Map;

/**
 * @author luzh
 * Create: 2019-12-17 10:11
 * Modified By:
 * Description:
 */
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "statistics_info", indexes = @Index(name = "time_index", columnList = "timestamp"))
//@Entity
//@DynamicUpdate
//@DynamicInsert
//@IdClass(StatisticsId.class)
//@EqualsAndHashCode(callSuper = false)
public class StatisticsDO extends BaseEntity {

    private static final long serialVersionUID = 9117478894075001876L;

    @Id
    @Column(name = "domain", columnDefinition = "varchar(255) NOT NULL comment'域名'")
    private String domain;
    @Id
    @Column(name = "timestamp", columnDefinition = "bigint NOT NULL comment'时间戳'")
    private Long timestamp;
    @Column(name = "minute", columnDefinition = "varchar(16) NOT NULL comment'日志时间-分钟'")
    private String minute;
    @Column(name = "all_bytes", columnDefinition = "bigint DEFAULT 0 comment'总字节数'")
    private Long allBytes;
    @Column(name = "all_count", columnDefinition = "bigint DEFAULT 0 comment'总请求数'")
    private Integer allCount;
    @Column(name = "upstream_bytes", columnDefinition = "bigint DEFAULT 0 comment'回源字节数'")
    private Long upstreamBytes;
    @Column(name = "upstream_count", columnDefinition = "int(10) DEFAULT 0 comment'请求回源数'")
    private Integer upstreamCount;
    @Column(name = "hit_bytes", columnDefinition = "bigint DEFAULT 0 comment'命中字节数'")
    private Long hitBytes;
    @Column(name = "hit_count", columnDefinition = "int(10) DEFAULT 0 comment'请求命中数'")
    private Integer hitCount;
    @Column(name = "response_status", columnDefinition = "varchar(255) DEFAULT '' comment'总字节数'")
    private String status;

    public StatisticsDO(StatisticsBO bo) {
        this.minute = bo.getMinute();
        this.domain = bo.getDomain();
        this.timestamp = bo.getTimestamp();
        this.allBytes = bo.getAllBytes();
        this.allCount = bo.getAllCount();
        this.upstreamBytes = bo.getUpstreamBytes();
        this.upstreamCount = bo.getUpstreamCount();
        this.hitBytes = bo.getHitBytes();
        this.hitCount = bo.getHitCount();
        this.status = JsonUtil.toJson(bo.getStatusMap());
    }

    public StatisticsDO(StatisticsId id) {
        this.timestamp = id.getTimestamp();
        this.domain = id.getDomain();
        this.allBytes = 0L;
        this.allCount = 0;
        this.upstreamBytes = 0L;
        this.upstreamCount = 0;
        this.hitBytes = 0L;
        this.hitCount = 0;
    }

    public void merge(StatisticsBO bo) {
        this.minute = bo.getMinute();
        this.allBytes += bo.getAllBytes();
        this.allCount += bo.getAllCount();
        this.upstreamBytes += bo.getUpstreamBytes();
        this.upstreamCount += bo.getUpstreamCount();
        this.hitBytes += bo.getHitBytes();
        this.hitCount += bo.getHitCount();
        if (StringUtil.isNullOrEmpty(this.status)) {
            this.status = JsonUtil.toJson(bo.getStatusMap());
        } else {
            Map<String, Integer> statusMap = getStatusMap();
            bo.getStatusMap().forEach((key, value) -> statusMap.put(key, statusMap.getOrDefault(key, 0) + value.intValue()));
            this.status = JsonUtil.toJson(statusMap);
        }
    }

    private Map<String, Integer> getStatusMap() {
        return JsonUtil.getStringIntegerMap(this.status);
    }

    @Override
    public Object getId() {
        return this.minute + ":" + this.domain;
    }
}
