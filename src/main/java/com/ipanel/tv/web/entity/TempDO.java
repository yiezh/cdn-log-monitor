package com.ipanel.tv.web.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @author luzh
 * Create: 2019-12-17 16:47
 * Modified By:
 * Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "temp_statistics_info")
@Entity
@DynamicInsert
@DynamicUpdate
public class TempDO extends BaseEntity {

    private static final long serialVersionUID = -8531615267447697594L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "timestamp", columnDefinition = "bigint NOT NULL comment'时间戳'")
    private Long timestamp;

    @Column(name = "domain", columnDefinition = "varchar(255) NOT NULL comment'域名'")
    private String domain;

    @Column(name = "content", columnDefinition = "varchar(10000) NOT NULL comment'数据内容'")
    private String content;

    @Column(name = "has_merge")
    private boolean hasMerge;

    public TempDO() {
        this.hasMerge = false;
    }
}
