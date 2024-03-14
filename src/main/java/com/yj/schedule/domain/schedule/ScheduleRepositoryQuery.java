package com.yj.schedule.domain.schedule;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ScheduleRepositoryQuery {
    Page<ScheduleResponseDto> findSchedules(ScheduleSearchCond cond, Pageable pageable);


    List<ScheduleResponseDto> findScheduleProjections();
}
