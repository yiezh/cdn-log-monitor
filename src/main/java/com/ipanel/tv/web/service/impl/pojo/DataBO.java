package com.ipanel.tv.web.service.impl.pojo;

import lombok.Data;
import org.aspectj.lang.annotation.DeclareAnnotation;

/**
 * @author luzh
 * Create: 2019-12-18 20:14
 * Modified By:
 * Description:
 */
@Data
public abstract class DataBO {
    public abstract long getTimestamp();

    public abstract String getMinute();
}
