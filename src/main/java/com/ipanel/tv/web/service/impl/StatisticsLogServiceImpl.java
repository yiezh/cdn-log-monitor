package com.ipanel.tv.web.service.impl;

import com.ipanel.tv.web.controller.response.*;
import com.ipanel.tv.web.dao.StatisticsRepository;
import com.ipanel.tv.web.entity.StatisticsId;
import com.ipanel.tv.web.entity.StatisticsDO;
import com.ipanel.tv.web.log.StatisticsBO;
import com.ipanel.tv.web.service.StatisticsLogService;
import com.ipanel.tv.web.service.impl.pojo.*;
import com.ipanel.tv.web.util.StringUtil;
import com.ipanel.tv.web.util.TimeUtil;
import com.ipanel.tv.web.util.jpa.partial.PartialQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author luzh
 * Create: 2019-12-17 11:06
 * Modified By:
 * Description:
 */
@Slf4j
@Service
public class StatisticsLogServiceImpl implements StatisticsLogService {

    private StatisticsRepository statisticsRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public StatisticsLogServiceImpl(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    @Override
    public void save(StatisticsBO bo) {
        StatisticsDO statisticsDO = new StatisticsDO(bo);
        statisticsRepository.save(statisticsDO);
    }

    @Transactional
    @Override
    public void merge(StatisticsBO bo) {
        StatisticsId id = new StatisticsId(bo);
        StatisticsDO statisticsDO = statisticsRepository.findById(id).orElse(new StatisticsDO(id));
        statisticsDO.merge(bo);
        statisticsRepository.save(statisticsDO);
    }

    @Override
    public DataListVO<AccessVO> getAccessData(String host, long startEpochSecond, long endEpochSecond, int timeGranularity) {
        return getData(AccessBO.class, AccessVO::new, host, startEpochSecond, endEpochSecond, timeGranularity, new AccessVO[0]);
    }

    /**
     * 通用的查询方法
     *
     * @param boClass          查询数据库结果bean
     * @param constructor      VO构造函数
     * @param host             域名
     * @param startEpochSecond 开始时间（由分钟转成Unix时间戳（秒））
     * @param endEpochSecond   结束时间（由分钟转成Unix时间戳（秒））
     * @param timeGranularity  时间粒度（分钟）
     * @param initialArray     VO数组
     * @param <VO>             VO class
     * @param <BO>             BO class
     * @return DataVO
     */
    private <VO extends DataVO, BO extends DataBO> DataListVO<VO> getData(Class<BO> boClass, DataConstructor<VO> constructor,
                                                                          String host, long startEpochSecond,
                                                                          long endEpochSecond, int timeGranularity,
                                                                          VO[] initialArray) {
        log.info("domain:{}, start:{}, end:{}, size:{}", host, startEpochSecond, endEpochSecond, timeGranularity);
        long debugTime = Instant.now().toEpochMilli();
        List<BO> dataList = queryData(entityManager, boClass, host, startEpochSecond, endEpochSecond);
        VO[] items = dataList.isEmpty() ? initialArray : aggregation(startEpochSecond, endEpochSecond, timeGranularity,
                initialArray, constructor, dataList);
        log.debug("total time: {}ms", Instant.now().toEpochMilli() - debugTime);
        return new DataListVO<>(items);
    }

    /**
     * 根据域名、开始时间、结束时间查询数据
     * 这里是查询部分字段组成新的对象，所以需要声明对应class
     *
     * @param entityManager    em
     * @param dataClass        新对象class
     * @param host             域名
     * @param startEpochSecond 开始时间（由分钟转成Unix时间戳（秒））
     * @param endEpochSecond   结束时间（由分钟转成Unix时间戳（秒））
     * @param <T>              新对象
     * @return list
     */
    private <T> List<T> queryData(EntityManager entityManager, Class<T> dataClass, String host, Long startEpochSecond,
                                  Long endEpochSecond) {
        long debugTime = Instant.now().toEpochMilli();
        Specification<StatisticsDO> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (!StringUtil.isNullOrEmpty(host)) {
                list.add(criteriaBuilder.equal(root.get("domain"), host));
            }
            list.add(criteriaBuilder.ge(root.get("timestamp"), startEpochSecond * 1000));
            list.add(criteriaBuilder.le(root.get("timestamp"), endEpochSecond * 1000));

            return criteriaBuilder.and(list.toArray(new Predicate[0]));
        };
        List<T> list = PartialQuery.statisticsQuery(entityManager, dataClass, StatisticsDO.class, specification);
        log.debug("query use time: {}", Instant.now().toEpochMilli() - debugTime);
        log.debug("data total: {}", list.size());
        return list;
    }

    /**
     * 将查到的数据按照时间粒度分组后再统计
     * 这里的大概逻辑是根据开始时间和结束时间以及时间粒度，判断最终会生成多少条数组，然后构造一个聚合对象数组
     * 然后遍历查到的数据，每条数据都有时间戳，根据开始时间、时间粒度，能得到每条数据所属聚合对象的在数组上的索引
     * 最终只需要遍历两次就可以处理完
     * 第一次遍历时间为O(n1)，n1为根据起止时间和时间粒度可以划分成多少组
     * 第二次遍历时间为O(n2)，n2为数据的容量
     *
     * @param startEpochSecond 开始时间（由分钟转成Unix时间戳（秒））
     * @param endEpochSecond   结束时间（由分钟转成Unix时间戳（秒））
     * @param timeGranularity  时间粒度（分钟）
     * @param initialArray     初始数组
     * @param constructor      聚合结果对象构造器
     * @param dataList         查询到的数据
     * @param <T>              聚合结果对象
     * @return 聚合结果数组
     */
    private <T extends DataVO> T[] aggregation(long startEpochSecond, long endEpochSecond, int timeGranularity, T[] initialArray,
                                               DataConstructor<T> constructor, List<? extends DataBO> dataList) {
        T[] array = generate(startEpochSecond, endEpochSecond, timeGranularity, initialArray, constructor);
        aggregation(dataList, array, startEpochSecond, timeGranularity);
        return array;
    }


    /**
     * 先按照开始时间和结束时间以及时间粒度初始化一个数组
     * 根据时间范围和时间粒度，将连续的时间分成n组
     * 然后使用for循环根据构造器初始化每一个聚合对象
     * 聚合对象构造器需要一个开始时间和结束时间
     *
     * @param startEpochSecond 开始时间（由分钟转成Unix时间戳（秒））
     * @param endEpochSecond   结束时间（由分钟转成Unix时间戳（秒））
     * @param timeGranularity  时间粒度（分钟）
     * @param initialArray     初始数组
     * @param constructor      聚合结果对象构造器
     * @param <T>              聚合结果对象
     * @return 聚合结果初始数组
     */
    @SuppressWarnings("unchecked")
    private <T extends DataVO> T[] generate(long startEpochSecond, long endEpochSecond, int timeGranularity,
                                            T[] initialArray, DataConstructor<T> constructor) {
        long debugTime = Instant.now().toEpochMilli();
        int arraySize = getArraySize(startEpochSecond, endEpochSecond, timeGranularity);
        Object[] array = new Object[arraySize];
        long start = startEpochSecond * 1000;
        long end;
        int interval = timeGranularity - 1;
        for (int i = 0; i < arraySize; i++) {
            if (i == arraySize - 1) {
                interval = (int) ((endEpochSecond * 1000 - start) / 1000 / 60);
            }
            end = start + TimeUtil.minuteEpochMilli(interval);
            array[i] = constructor.construct(start, end);
            start += TimeUtil.minuteEpochMilli(timeGranularity);
        }
        T[] result = (T[]) Arrays.copyOf(array, arraySize, initialArray.getClass());
        log.debug("make array use time: {}ms, array size: {}", Instant.now().toEpochMilli() - debugTime, arraySize);
        return result;
    }

    /**
     * 根据时间范围和时间粒度计算数组容量，需要注意的是开始时间和结束时间都要包含进去，所以length要+1
     *
     * @param startEpochSecond 开始时间（由分钟转成Unix时间戳（秒））
     * @param endEpochSecond   结束时间（由分钟转成Unix时间戳（秒））
     * @param timeGranularity  时间粒度（分钟）
     * @return 数组容量
     */
    private static int getArraySize(long startEpochSecond, long endEpochSecond, int timeGranularity) {
        long length = (endEpochSecond - startEpochSecond) / 60 + 1;
        return (int) (length % timeGranularity == 0 ? length / timeGranularity : length / timeGranularity + 1);
    }

    /**
     * 聚合数据
     * 遍历数据的时候，根据数据的具体时间戳就能准确找到对应聚合对象在数组上的位置，每条数据的聚合操作时间复杂度为O(1)
     *
     * @param dataList         数据
     * @param array            聚合数组
     * @param startEpochSecond 起始时间
     * @param timeGranularity  时间粒度
     * @param <T>              聚合对象
     */
    private <T extends DataVO> void aggregation(List<? extends DataBO> dataList, T[] array, long startEpochSecond,
                                                int timeGranularity) {
        long debugTime = Instant.now().toEpochMilli();
        dataList.forEach(bo -> {
            int index = getIndex(bo.getTimestamp() / 1000, startEpochSecond, timeGranularity);
            T item = array[index];
            item.add(bo);
        });
        log.debug("process data use time: {}", Instant.now().toEpochMilli() - debugTime);
    }

    /**
     * 根据数据的Unix时间戳（秒）、数组起始位置的Unix时间戳（秒）、数据粒度得出数据所属聚合对象在聚合数组上的索引
     *
     * @param dataEpochSecond  数据时间
     * @param startEpochSecond 起始时间
     * @param timeGranularity  时间粒度
     * @return index
     */
    private static int getIndex(long dataEpochSecond, long startEpochSecond, int timeGranularity) {
        return (dataEpochSecond - startEpochSecond) == 0 ? 0 : (int) ((dataEpochSecond - startEpochSecond) / timeGranularity / 60);
    }

    @Override
    public DataListVO<HitVO> getHitData(String host, long startEpochSecond, long endEpochSecond, int timeGranularity) {
        return getData(HitBO.class, HitVO::new, host, startEpochSecond, endEpochSecond, timeGranularity, new HitVO[0]);
    }

    @Override
    public DataListVO<StatusVO> getStatusData(String host, long startEpochSecond, long endEpochSecond, int timeGranularity) {
        return getData(StatusBO.class, StatusVO::new, host, startEpochSecond, endEpochSecond, timeGranularity, new StatusVO[0]);
    }

    @Override
    public DataListVO<UpstreamVO> getUpstreamData(String host, long startEpochSecond, long endEpochSecond, int timeGranularity) {
        return getData(UpstreamBO.class, UpstreamVO::new, host, startEpochSecond, endEpochSecond, timeGranularity, new UpstreamVO[0]);
    }
}