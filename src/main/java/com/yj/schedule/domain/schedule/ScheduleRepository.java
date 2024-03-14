package com.yj.schedule.domain.schedule;

import com.yj.schedule.domain.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepositoryQuery {
    List<Schedule> findAllByDoneEqualsOrderByCreatedAtDesc(String done);
}
