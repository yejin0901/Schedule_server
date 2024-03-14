package com.yj.schedule.domain.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ScheduleProjection {
    private Long id;
    private String title;
    private String contents;
}
