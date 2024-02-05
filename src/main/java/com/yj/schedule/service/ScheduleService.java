package com.yj.schedule.service;


import com.yj.schedule.dto.ScheduleRequestDto;
import com.yj.schedule.dto.ScheduleResponseDto;
import com.yj.schedule.entity.Schedule;
import com.yj.schedule.entity.User;
import com.yj.schedule.jwt.JwtUtil;
import com.yj.schedule.repository.ScheduleRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final JwtUtil jwtUtil;

    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto, User user, HttpServletRequest request) {
        String token = jwtUtil.getJwtFromHeader(request);
        jwtUtil.validateToken(token);
        Schedule schedule = scheduleRepository.save(new Schedule(requestDto, user));
        return new ScheduleResponseDto(schedule);


    }

    public ScheduleResponseDto getSchedule(Long id, User user) {
        Schedule schedule = findSchedule(id);
        if(!schedule.getUser().equals(user.getUsername())){
            new ScheduleResponseDto("fail-validate-user");
        }
        return new ScheduleResponseDto(schedule);


    }

    private Schedule findSchedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 할일이 존재하지 않습니다."));
    }

    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto requestDto, User user, HttpServletRequest request) {
        String token = jwtUtil.getJwtFromHeader(request);
        jwtUtil.validateToken(token);

        Schedule schedule = findSchedule(id);

        if(!checkSelfUser(user,schedule)){
            return new ScheduleResponseDto(("fail-validate-user"));
        }
        if (requestDto.getDone().equals("TRUE")) {
            schedule.setDone("TRUE");
        }
        schedule.setTitle(requestDto.getTitle());
        schedule.setContents(requestDto.getContents());
        schedule = scheduleRepository.save(schedule);

        return new ScheduleResponseDto(schedule);


    }


    public List<ScheduleResponseDto> getAllSchedule() {
        return scheduleRepository.findAllByDoneEqualsOrderByCreatedAtDesc("FALSE").stream().map(ScheduleResponseDto::new).toList();
    }

    private Boolean checkSelfUser(User user, Schedule schedule) {
        return user.getId().equals(schedule.getId());

    }


}
