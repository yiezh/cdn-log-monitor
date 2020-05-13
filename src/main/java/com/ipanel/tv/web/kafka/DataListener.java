package com.ipanel.tv.web.kafka;

import com.ipanel.tv.web.log.LogBO;
import com.ipanel.tv.web.log.LogConverter;
import com.ipanel.tv.web.task.AsyncTask;
import com.ipanel.tv.web.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author luzh
 * Create: 2019-12-16 16:36
 * Modified By:
 * Description:
 */
@Slf4j
@Component
public class DataListener {

    private AsyncTask asyncTask;

    public DataListener(AsyncTask asyncTask) {
        this.asyncTask = asyncTask;
    }

    @KafkaListener(topics = "test-log-original", containerFactory = "kafkaListenerContainerFactory")
    public void originalLog(List<String> content) {
        log.info("{}", content.size());
        List<LogBO> list = content.parallelStream().map(value -> JsonUtil.fromJson(value, LogBO.class))
                .filter(Objects::nonNull).map(LogConverter::convertTime).collect(Collectors.toList());
        asyncTask.aggregation(list);
    }

}
