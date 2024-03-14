package com.yj.schedule.domain.schedule;


import com.yj.schedule.domain.common.PageDTO;
import com.yj.schedule.domain.user.User;
import com.yj.schedule.global.exception.IllegalAccessException;
import com.yj.schedule.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleServiceImpl implements ScheduleService{

    private final ScheduleRepository scheduleRepository;
    @Transactional
    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto, User user) {
        Schedule schedule = scheduleRepository.save(new Schedule(requestDto, user));
        return new ScheduleResponseDto(schedule);
    }

    public Page<ScheduleResponseDto> getUserSchedule(User user) {
        PageDTO pageDTO = PageDTO.builder().currentPage(1).size((int) scheduleRepository.count()).build();
        ScheduleSearchCond cond = ScheduleSearchCond.builder().userId(user.getId()).build();
        return scheduleRepository.findSchedules(cond,pageDTO.toPageable());
    }

    public Schedule findSchedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new NotFoundException("해당 할일이 존재하지 않습니다."));
    }

    @Transactional
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto requestDto, User user) {
        Schedule schedule = findSchedule(id);

        System.out.println("find : " + schedule.getId());

        if(!checkSelfUser(user,schedule)){
            throw new IllegalAccessException("해당 사용자가 아닙니다.");
        }
        if (requestDto.getDone().equals("TRUE")) {
            schedule.setDone("TRUE");
        }

        schedule.setTitle(requestDto.getTitle());
        schedule.setContents(requestDto.getContents());

        return new ScheduleResponseDto(schedule);
    }

    public Page<ScheduleResponseDto> getAllSchedule() {
        PageDTO pageDTO = PageDTO.builder().currentPage(1).size((int) scheduleRepository.count()).build();
        ScheduleSearchCond cond = ScheduleSearchCond.builder().IsDone("False").build();
        return scheduleRepository.findSchedules(cond, pageDTO.toPageable());
    }

    private Boolean checkSelfUser(User user, Schedule schedule) {
        return user.getUsername().equals(schedule.getUser().getUsername());
    }
}
