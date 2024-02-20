package com.yj.schedule.service;


import com.yj.schedule.dto.ScheduleRequestDto;
import com.yj.schedule.dto.ScheduleResponseDto;
import com.yj.schedule.entity.Schedule;
import com.yj.schedule.entity.User;
import com.yj.schedule.jwt.JwtUtil;
import com.yj.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto, User user) {
        Schedule schedule = scheduleRepository.save(new Schedule(requestDto, user));
        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    public ScheduleResponseDto getSchedule(Long id, User user) {
        Schedule schedule = findSchedule(id);
        if(!checkSelfUser(user,schedule)){
            log.info("ok");
            throw new RejectedExecutionException("해당 사용자가 아닙니다.");
        }
        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    public Schedule findSchedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 할일이 존재하지 않습니다."));
    }

    @Transactional
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto requestDto, User user) {
        Schedule schedule = findSchedule(id);

        System.out.println("find : " + schedule.getId());

        if(!checkSelfUser(user,schedule)){
            throw new RejectedExecutionException("해당 사용자가 아닙니다.");
        }
        if (requestDto.getDone().equals("TRUE")) {
            schedule.setDone("TRUE");
        }

        schedule.setTitle(requestDto.getTitle());
        schedule.setContents(requestDto.getContents());
        scheduleRepository.save(schedule);



        return new ScheduleResponseDto(schedule);
    }

    public List<ScheduleResponseDto> getAllSchedule() {
        return scheduleRepository.findAllByDoneEqualsOrderByCreatedAtDesc("FALSE").stream().map(ScheduleResponseDto::new).toList();
    }

    private Boolean checkSelfUser(User user, Schedule schedule) {
        log.info(schedule.getUser().getUsername());
        return user.getUsername().equals(schedule.getUser().getUsername());
    }
}
