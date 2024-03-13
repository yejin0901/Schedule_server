package com.yj.schedule.domain.schedule;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleRequestDto {
    private String title;
    private String contents;
    private String done;
}
