package com.ipanel.tv.web.log;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author luzh
 * Create: 2019-12-10 18:00
 * Modified By:
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogBO {
    private String minute;
    private String status;
    @SerializedName("body_bytes_sent")
    private Long bodyBytes;
    private String domain;
    @SerializedName("cache_hit")
    private String cacheHit;
    @SerializedName("log_time")
    private String logTime;
    private Long timestamp;

    boolean cacheHit() {
        return !("-".equals(cacheHit) || "".equals(cacheHit));
    }
}
