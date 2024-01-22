package com.yj.schedule.entity;

import com.yj.schedule.dto.ScheduleRequestDto;
import com.yj.schedule.dto.ScheduleResponseDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "schedule")
public class Schedule extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "admin", nullable = false)
    private String admin;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "contents", nullable = false, length = 500)
    private String contents;

    public Schedule(ScheduleRequestDto requestDto){
        this.title = requestDto.getTitle();
        this.admin = requestDto.getAdmin();
        this.password = requestDto.getPassword();
        this.contents = requestDto.getContents();

    }

    public void update(ScheduleRequestDto requestDto){
        this.title = requestDto.getTitle();
        this.admin = requestDto.getAdmin();
        this.password = requestDto.getPassword();
        this.contents = requestDto.getContents();
    }




}
