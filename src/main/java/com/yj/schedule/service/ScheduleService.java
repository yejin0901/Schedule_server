package com.yj.schedule.service;

import com.yj.schedule.dto.ScheduleRequestDto;
import com.yj.schedule.dto.ScheduleResponseDto;
import com.yj.schedule.entity.Schedule;
import com.yj.schedule.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto) {
        Schedule schedule = new Schedule(requestDto);
        Schedule saveSchedule = scheduleRepository.save(schedule);
        ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto(saveSchedule);
        return scheduleResponseDto;

    }

    public List<ScheduleResponseDto> getSchedules() {
        return scheduleRepository.findAllByOrderByCreatedAtDesc().stream().map(ScheduleResponseDto::new).toList();
    }

    public ScheduleResponseDto getDetailSchedule(Long id) {
        Schedule schedule = findSchedule(id);
        return new ScheduleResponseDto(schedule);
    }


    public ScheduleResponseDto deleteSchedule(Long id, String password) {
        Schedule schedule = findSchedule(id);
        try {
            checkPassword(schedule, password);
        } catch (Exception e) {
            return new ScheduleResponseDto("HTTP status code > " + HttpStatus.NOT_FOUND.value());
        }
        scheduleRepository.delete(schedule);
        ScheduleResponseDto responseDto = new ScheduleResponseDto("삭제 성공");

        return responseDto;
    }

    private Schedule findSchedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("선택한 메모는 존재하지 않습니다."));
    }


    @Transactional
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto requestDto) {
        Schedule schedule = findSchedule(id);
        try {
            checkPassword(schedule, requestDto.getPassword());
        } catch (Exception e) {
            return new ScheduleResponseDto("HTTP status code > " + HttpStatus.NOT_FOUND.value());
        }
        schedule.update(requestDto);
        return new ScheduleResponseDto(schedule);
    }


    public void checkPassword(Schedule schedule, String password) {
        if (!schedule.getPassword().equals(password)) {
            throw new IncorrectPasswordException("비밀번호 불일치");
        }
    }

    public class IncorrectPasswordException extends IllegalArgumentException {
        public IncorrectPasswordException(String message) {
            super(message);
        }
    }


}
