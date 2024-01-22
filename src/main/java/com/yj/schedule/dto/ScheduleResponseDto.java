package com.yj.schedule.dto;


import lombok.Getter;
import com.yj.schedule.entity.Schedule;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


@Getter
public class ScheduleResponseDto {
    private Long id;
    private String contents;
    private String title;
    private String admin;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ScheduleResponseDto(Schedule schedule){
        this.id = schedule.getId();
        this.contents = schedule.getContents();
        this.title = schedule.getTitle();
        this.admin = schedule.getAdmin();
        this.modifiedAt = schedule.getModifiedAt();
        this.createdAt = schedule.getCreatedAt();
    }
}
