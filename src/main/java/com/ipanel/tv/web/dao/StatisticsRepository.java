package com.ipanel.tv.web.dao;

import com.ipanel.tv.web.entity.StatisticsId;
import com.ipanel.tv.web.entity.StatisticsDO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author luzh
 * Create: 2019-12-17 10:34
 * Modified By:
 * Description:
 */
@Repository
public interface StatisticsRepository extends CrudRepository<StatisticsDO, StatisticsId> {

}
