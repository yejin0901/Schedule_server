package com.yj.schedule.domain.schedule;

import com.yj.schedule.domain.user.User;
import org.springframework.data.domain.Page;

public interface ScheduleService {
    ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto, User user);

    Page<ScheduleResponseDto> getUserSchedule(User user);

    ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto requestDto, User user);
}
