package com.ipanel.tv.web.log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luzh
 * Create: 2019-12-11 16:42
 * Modified By:
 * Description:
 */
@Data
@AllArgsConstructor
public class StatisticsBO {
    private String minute;
    private String domain;
    private long timestamp;
    private long allBytes;
    private int allCount;
    private long upstreamBytes;
    private int upstreamCount;
    private long hitBytes;
    private int hitCount;
    private Map<String, Long> statusMap;
    private List<String> statusList;

    public StatisticsBO() {
        this.statusList = new ArrayList<>(100000);
        this.statusMap = new HashMap<>(10);
    }

    public void addSource(LogBO log) {
        this.minute = log.getMinute();
        this.domain = log.getDomain();
        this.timestamp = log.getTimestamp();
        this.allBytes += log.getBodyBytes();
        this.allCount += 1;
        if (log.cacheHit()) {
            this.hitBytes += log.getBodyBytes();
            this.hitCount += 1;
        } else {
            this.upstreamBytes += log.getBodyBytes();
            this.upstreamCount += 1;
        }
        statusList.add(log.getStatus());
    }

    public void merge(StatisticsBO bo) {
        this.minute = bo.getMinute();
        this.domain = bo.getDomain();
        this.timestamp = bo.getTimestamp();
        this.allBytes += bo.getAllBytes();
        this.allCount += bo.getAllCount();
        this.hitBytes += bo.getHitBytes();
        this.hitCount += bo.getHitCount();
        this.upstreamBytes += bo.getUpstreamBytes();
        this.upstreamCount += bo.getUpstreamCount();
        bo.getStatusMap().forEach((key, value) -> {
            this.statusMap.put(key, this.statusMap.getOrDefault(key, 0L) + value);
        });
    }

    public String toString() {
        return "(" +
                "minute:" + this.minute + "," +
                "host:" + this.domain + "," +
                "timestamp:" + this.timestamp + "," +
                "allBytes:" + this.allBytes + "," +
                "allCount:" + this.allCount + "," +
                "upstreamBytes:" + this.upstreamBytes + "," +
                "upstreamCount:" + this.upstreamCount + "," +
                "hitBytes:" + this.hitBytes + "," +
                "hitCount:" + this.hitCount + "," +
                "statusList:" + this.statusList.size() + "," +
                "statusMap:" + this.statusMap +
                ")";
    }
}
