package com.yj.schedule.domain.schedule;

import com.yj.schedule.domain.comment.Comment;
import com.yj.schedule.domain.Timestamped;
import com.yj.schedule.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "schedule")
public class Schedule extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false, length = 500)
    private String contents;
    @Column(nullable = false)
    private String done;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(
            mappedBy = "schedule", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Comment> commentList = new ArrayList<>();

    public Schedule(ScheduleRequestDto requestDto, User user){
        this.title = requestDto.getTitle();
        this.user = user;
        this.contents = requestDto.getContents();
        this.done = "FALSE";

    }




}
