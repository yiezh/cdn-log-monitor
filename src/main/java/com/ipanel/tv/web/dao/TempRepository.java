package com.ipanel.tv.web.dao;

import com.ipanel.tv.web.entity.TempDO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author luzh
 * Create: 2019-12-17 16:54
 * Modified By:
 * Description:
 */
@Repository
public interface TempRepository extends CrudRepository<TempDO, Long> {

    List<TempDO> findByHasMerge(boolean hasMerge);
}
