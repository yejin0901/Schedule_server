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


    public Long deleteSchedule(Long id, String password) {
        Schedule schedule = findSchedule(id);
        checkPassword(schedule, password);
        scheduleRepository.delete(schedule);
        return id;
    }

    private Schedule findSchedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("선택한 메모는 존재하지 않습니다."));
    }


    @Transactional
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto requestDto) {
        Schedule schedule = findSchedule(id);
        checkPassword(schedule, requestDto.getPassword());
        schedule.update(requestDto);
        return new ScheduleResponseDto(schedule);
    }

    public void checkPassword(Schedule schedule, String password) {
        if (!schedule.getPassword().equals(password)) {
            throw new IncorrectPasswordException("비밀번호가 틀립니다. http status code : " + HttpStatus.BAD_REQUEST.value());
        }
    }

    public class IncorrectPasswordException extends IllegalArgumentException {
        public IncorrectPasswordException(String message) {
            super(message);
        }
    }


}
