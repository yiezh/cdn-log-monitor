package com.ipanel.tv.web.controller;

import com.ipanel.tv.web.controller.request.CommonReq;
import com.ipanel.tv.web.controller.response.*;
import com.ipanel.tv.web.exception.GlobalException;
import com.ipanel.tv.web.service.StatisticsLogService;
import com.ipanel.tv.web.util.TimeUtil;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author luzh
 * Create: 2019-12-18 10:04
 * Modified By:
 * Description:
 */
@RestController
@RequestMapping("statistics")
public class StatisticsLogController {

    private static final DateTimeFormatter DATE_TIME_FORMATTER_MINUTE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private StatisticsLogService statisticsLogService;

    public StatisticsLogController(StatisticsLogService statisticsLogService) {
        this.statisticsLogService = statisticsLogService;
    }

    @ModelAttribute
    public void getRequest(@ModelAttribute @Valid CommonReq req, Model model) throws GlobalException {
        LocalDateTime start = TimeUtil.parse(req.getStart(), DATE_TIME_FORMATTER_MINUTE);
        LocalDateTime end = TimeUtil.parse(req.getEnd(), DATE_TIME_FORMATTER_MINUTE);
        if (end.isBefore(start) || end.isEqual(start)) {
            throw new GlobalException("开始、结束时间无效！");
        }
        if (end.isAfter(LocalDateTime.now())) {
            throw new GlobalException("结束时间不能超过当前时间！");
        }
        model.addAttribute("startEpochSecond", start.withSecond(0).toEpochSecond(TimeUtil.getSystemDefaultZoneId()));
        model.addAttribute("endEpochSecond", end.withSecond(0).toEpochSecond(TimeUtil.getSystemDefaultZoneId()));
    }

    @GetMapping("access")
    public BaseVO<DataListVO<AccessVO>> accessData(@ModelAttribute @Valid CommonReq req,
                                                   @ModelAttribute("startEpochSecond") Long startEpochSecond,
                                                   @ModelAttribute("endEpochSecond") Long endEpochSecond) {
        return new BaseVO<>(statisticsLogService.getAccessData(req.getDomain(), startEpochSecond, endEpochSecond, req.getTimeGranularity()));
    }

    @GetMapping("hit")
    public BaseVO<DataListVO<HitVO>> hitData(@ModelAttribute @Valid CommonReq req,
                                             @ModelAttribute("startEpochSecond") Long startEpochSecond,
                                             @ModelAttribute("endEpochSecond") Long endEpochSecond) {
        return new BaseVO<>(statisticsLogService.getHitData(req.getDomain(), startEpochSecond, endEpochSecond, req.getTimeGranularity()));
    }

    @GetMapping("status")
    public BaseVO<DataListVO<StatusVO>> statusData(@ModelAttribute @Valid CommonReq req,
                                                   @ModelAttribute("startEpochSecond") Long startEpochSecond,
                                                   @ModelAttribute("endEpochSecond") Long endEpochSecond) {
        return new BaseVO<>(statisticsLogService.getStatusData(req.getDomain(), startEpochSecond, endEpochSecond, req.getTimeGranularity()));
    }

    @GetMapping("upstream")
    public BaseVO<DataListVO<UpstreamVO>> upstreamData(@ModelAttribute @Valid CommonReq req,
                                                       @ModelAttribute("startEpochSecond") Long startEpochSecond,
                                                       @ModelAttribute("endEpochSecond") Long endEpochSecond) {
        return new BaseVO<>(statisticsLogService.getUpstreamData(req.getDomain(), startEpochSecond, endEpochSecond, req.getTimeGranularity()));
    }
}
