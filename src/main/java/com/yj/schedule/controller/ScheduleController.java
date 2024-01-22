package com.yj.schedule.controller;


import org.springframework.http.HttpStatus;
import com.yj.schedule.dto.ScheduleResponseDto;
import com.yj.schedule.dto.ScheduleRequestDto;
import com.yj.schedule.service.ScheduleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/schedules")
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto requestDto) {
        return scheduleService.createSchedule(requestDto);
    }

    @GetMapping("/schedules/all")
    public List<ScheduleResponseDto> getSchedules() {
        return scheduleService.getSchedules();
    }

    @GetMapping("/schedules/{id}")
    public ScheduleResponseDto getDetailSchedule(@PathVariable Long id) {
        return scheduleService.getDetailSchedule(id);
    }


    @PutMapping("/schedules/{id}")
    public ScheduleResponseDto updateSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto requestDto) {
        return scheduleService.updateSchedule(id, requestDto);
    }

    @DeleteMapping("/schedules/{id}")
    public Long deleteSchedule(@PathVariable Long id, @RequestParam String password) {
        return scheduleService.deleteSchedule(id, password);
    }

}
