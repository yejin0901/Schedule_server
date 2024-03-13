package com.yj.schedule.domain.comment;


import com.yj.schedule.domain.schedule.Schedule;
import com.yj.schedule.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String comments;

    @Column(nullable = false, length = 200)
    private String creator;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    public Comment(CommentRequestDto requestDto, Schedule schedule, User user){
        this.schedule = schedule;
        this.comments = requestDto.getComments();
        this.creator = user.getUsername();
    }

}
