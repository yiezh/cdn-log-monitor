package com.ipanel.tv.web.entity;

import com.ipanel.tv.web.log.StatisticsBO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @author luzh
 * Create: 2019-12-17 10:11
 * Modified By:
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "test_1_info", indexes = @Index(name = "time_index", columnList = "timestamp"))
@Entity
@DynamicUpdate
@DynamicInsert
@EqualsAndHashCode(callSuper = false)
public class AliNginxDO extends BaseEntity {

    private static final long serialVersionUID = 9117478894075001876L;

    @Id
    @Column(name = "timestamp", columnDefinition = "bigint NOT NULL comment'时间戳'")
    private Long timestamp;
    @Column(name = "minute", columnDefinition = "varchar(16) NOT NULL comment'日志时间-分钟'")
    private String minute;
    @Column(name = "all_bytes", columnDefinition = "bigint DEFAULT 0 comment'总字节数'")
    private Long allBytes;
    @Column(name = "all_count", columnDefinition = "bigint DEFAULT 0 comment'总请求数'")
    private Integer allCount;

    public AliNginxDO(StatisticsBO bo) {
        this.minute = bo.getMinute();
        this.timestamp = bo.getTimestamp();
        this.allBytes = bo.getAllBytes();
        this.allCount = bo.getAllCount();
    }

    public AliNginxDO(StatisticsId id) {
        this.timestamp = id.getTimestamp();
        this.allBytes = 0L;
        this.allCount = 0;
    }

    public void merge(StatisticsBO bo) {
        this.minute = bo.getMinute();
        this.allBytes += bo.getAllBytes();
        this.allCount += bo.getAllCount();
    }

    @Override
    public Object getId() {
        return this.timestamp;
    }
}
