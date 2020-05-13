package com.ipanel.tv.web.service.impl;

import com.ipanel.tv.web.dao.TempRepository;
import com.ipanel.tv.web.entity.TempDO;
import com.ipanel.tv.web.log.StatisticsBO;
import com.ipanel.tv.web.pojo.StatisticsLogGroupKey;
import com.ipanel.tv.web.service.MergeService;
import com.ipanel.tv.web.service.StatisticsLogService;
import com.ipanel.tv.web.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author luzh
 * Create: 2019-12-17 17:21
 * Modified By:
 * Description:
 */
@Slf4j
@Service
public class MergeServiceImpl implements MergeService {

    private static final List<String> EMPTY_LIST = new ArrayList<>(0);

    private StatisticsLogService statisticsLogService;
    private TempRepository tempRepository;

    public MergeServiceImpl(StatisticsLogService statisticsLogService, TempRepository tempRepository) {
        this.statisticsLogService = statisticsLogService;
        this.tempRepository = tempRepository;
    }

    @Override
    public void saveStatisticsBO(StatisticsBO bo) {
        bo.setStatusList(EMPTY_LIST);
        TempDO tempDO = new TempDO();
        tempDO.setTimestamp(bo.getTimestamp());
        tempDO.setDomain(bo.getDomain());
        tempDO.setContent(JsonUtil.toJson(bo));
        tempDO.setHasMerge(false);
        tempRepository.save(tempDO);
    }

    @Transactional
    @Override
    public void merge() {
        List<TempDO> list = tempRepository.findByHasMerge(false);
        list.stream()
                .collect(Collectors.groupingBy(value -> new StatisticsLogGroupKey(value.getTimestamp(), value.getDomain())))
                .forEach((key, value) -> {
                    StatisticsBO bo = new StatisticsBO();
                    value.forEach(log -> {
                        StatisticsBO bo1 = JsonUtil.fromJson(log.getContent(), StatisticsBO.class);
                        if (null == bo1) {
                            return;
                        }
                        bo.merge(bo1);
                        tempRepository.deleteById(log.getId());
                    });
                    statisticsLogService.merge(bo);
                });
    }
}
