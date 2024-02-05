package com.yj.schedule.repository;

import com.yj.schedule.dto.ScheduleResponseDto;
import com.yj.schedule.entity.Schedule;
import com.yj.schedule.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByDoneEqualsOrderByCreatedAtDesc(String done);
}
