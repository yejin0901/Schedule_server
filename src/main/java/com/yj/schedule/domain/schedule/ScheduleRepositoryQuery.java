package com.yj.schedule.domain.schedule;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScheduleRepositoryQuery {
    Page<ScheduleResponseDto> findSchedules(ScheduleSearchCond cond, Pageable pageable);

}
