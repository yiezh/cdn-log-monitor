package com.ipanel.tv.web.task;

import com.ipanel.tv.web.service.MergeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author luzh
 * Create: 2019-12-17 17:16
 * Modified By:
 * Description:
 */
@Slf4j
@Component
public class ScheduleTask {

    private MergeService mergeService;

    public ScheduleTask(MergeService mergeService) {
        this.mergeService = mergeService;
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void mergeLateLog() {
        log.info("merge log ~ {}", LocalDateTime.now().toString());
        mergeService.merge();
    }
}
