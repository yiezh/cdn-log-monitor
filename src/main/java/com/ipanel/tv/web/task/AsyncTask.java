package com.ipanel.tv.web.task;

import com.ipanel.tv.web.log.LogBO;
import com.ipanel.tv.web.log.StatisticsBO;
import com.ipanel.tv.web.pojo.StatisticsLogGroupKey;
import com.ipanel.tv.web.service.MergeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author luzh
 * Create: 2019-12-17 09:43
 * Modified By:
 * Description:
 */
@Slf4j
@Component
public class AsyncTask {

    private int count = 0;

    private MergeService mergeService;

    public AsyncTask(MergeService mergeService) {
        this.mergeService = mergeService;
    }

    /**
     * 将日志统计好之后汇总到数据库临时表中
     *
     * @param content list
     */
    @Async
    public void aggregation(List<LogBO> content) {
        log.debug("receive numbers: {}", content.size());
        count += content.size();
        content.parallelStream()
                .collect(Collectors.groupingBy(log -> new StatisticsLogGroupKey(log.getTimestamp(), log.getDomain())))
                .forEach((key, list) -> {
                    StatisticsBO bo = new StatisticsBO();
                    list.forEach(bo::addSource);
                    Map<String, Long> statusMap = bo.getStatusList().stream()
                            .collect(Collectors.groupingBy(String::toString, Collectors.counting()));
                    bo.setStatusMap(statusMap);
                    mergeService.saveStatisticsBO(bo);
                });
        log.debug("total: {}", count);
    }
}
